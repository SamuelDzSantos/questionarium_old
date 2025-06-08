package br.com.questionarium.user_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronization;

import br.com.questionarium.entities.Email;
import br.com.questionarium.entities.UserCreatedEvent;
import br.com.questionarium.types.Role;
import br.com.questionarium.user_service.dto.CreateUserRequest;
import br.com.questionarium.user_service.dto.UpdateUserRequest;
import br.com.questionarium.user_service.dto.UserResponse;
import br.com.questionarium.user_service.exception.EmailAlreadyExistsException;
import br.com.questionarium.user_service.exception.TokenInvalidException;
import br.com.questionarium.user_service.exception.UserNotFoundException;
import br.com.questionarium.user_service.model.User;
import br.com.questionarium.user_service.model.VerificationToken;
import br.com.questionarium.user_service.repository.UserRepository;
import br.com.questionarium.user_service.repository.VerificationTokenRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final RabbitTemplate rabbitTemplate;

    // Se preferir externalizar via application.properties, use
    // @Value("${app.base-url}") aqui
    private static final String BASE_URL = "http://localhost:14002";
    private static final String USER_EXCHANGE = "user-exchange";
    private static final String ROUTING_KEY_CREATED = "user.created";
    private static final String ROUTING_KEY_EMAIL = "user.email";

    public UserService(UserRepository userRepository,
            VerificationTokenRepository tokenRepository,
            RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 1) Verifica duplicidade de e-mail.
     * 2) Salva usuário com active=false.
     * 3) Gera e salva VerificationToken.
     * 4) Publica UserCreatedEvent para AuthService após commit.
     * 5) Publica Email para EmailService após commit.
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // 1) Verifica duplicidade de e-mail
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistsException(request.getEmail());
        });

        // 2) Salva entidade User com active=false
        User user = new User(request.getName(), request.getEmail(), request.getRoles());
        user.setActive(false);
        User savedUser = userRepository.save(user);
        logger.info("Usuário salvo com ID {}", savedUser.getId());

        // 3) Gera token de verificação e salva
        VerificationToken token = new VerificationToken(savedUser);
        tokenRepository.save(token);
        logger.info("VerificationToken gerado para usuário ID {}: {}", savedUser.getId(), token.getToken());

        // 4) Publicação de eventos só após commit bem-sucedido
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 4-a) Publica evento de usuário criado para o AuthService
                String roleString = request.getRoles().get(0);
                Role role = Role.valueOf(roleString);
                UserCreatedEvent event = new UserCreatedEvent(
                        savedUser.getId(),
                        savedUser.getEmail(),
                        request.getPassword(),
                        role);
                try {
                    rabbitTemplate.convertAndSend(USER_EXCHANGE, ROUTING_KEY_CREATED, event);
                    logger.info("UserCreatedEvent publicado para usuário ID {}", savedUser.getId());
                } catch (Exception e) {
                    logger.error("Erro ao publicar UserCreatedEvent para usuário ID {}", savedUser.getId(), e);
                }

                // 4-b) Monta e publica o objeto Email para o EmailService
                String confirmLink = BASE_URL + "/users/confirm?token=" + token.getToken();
                String subject = "Confirme seu cadastro";
                String body = ""
                        + "Olá, " + savedUser.getName() + "!\n\n"
                        + "Clique no link abaixo para confirmar seu e-mail e ativar sua conta:\n"
                        + confirmLink + "\n\n"
                        + "Este link expira em 24 horas.";

                Email emailMessage = new Email(subject, body, savedUser.getEmail());
                try {
                    rabbitTemplate.convertAndSend(USER_EXCHANGE, ROUTING_KEY_EMAIL, emailMessage);
                    logger.info("Email de confirmação enviado para {}", savedUser.getEmail());
                } catch (Exception e) {
                    logger.error("Erro ao enviar email de confirmação para {}", savedUser.getEmail(), e);
                }
            }
        });

        // 5) Retorna a resposta para o controlador
        return mapToResponse(savedUser);
    }

    /**
     * 1) Busca token. Se não existir, lança TokenInvalidException.
     * 2) Valida expiração. Se expirado, deleta e lança TokenInvalidException.
     * 3) Marca usuário como ativo e remove todos os tokens antigos.
     * 4) Retorna mensagem de sucesso.
     */
    @Transactional
    public String confirmUser(String tokenStr) {
        VerificationToken token = tokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new TokenInvalidException("Token inválido"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new TokenInvalidException("Token expirado");
        }

        User user = token.getUser();
        user.setActive(true);
        userRepository.save(user);
        logger.info("Usuário ID {} confirmado e ativado", user.getId());

        tokenRepository.deleteByUser_Id(user.getId());
        logger.info("Tokens relacionados ao usuário ID {} removidos", user.getId());

        return "Usuário confirmado com sucesso.";
    }

    /**
     * Retorna lista de todos os usuários ativos (active = true).
     */
    @Transactional(readOnly = true)
    public List<UserResponse> listAllUsers() {
        List<User> usersAtivos = userRepository.findAllByActiveTrue();
        return usersAtivos.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retorna um usuário ativo (active=true) pelo ID, ou lança
     * UserNotFoundException.
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToResponse(user);
    }

    /**
     * Atualiza nome e roles de um usuário ativo.
     * Se não encontrar, lança UserNotFoundException.
     * Retorna UserResponse atualizado.
     */
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(request.getName());
        user.setRoles(request.getRoles());
        User updated = userRepository.save(user);
        logger.info("Usuário ID {} atualizado", updated.getId());

        return mapToResponse(updated);
    }

    /**
     * Marca o usuário como inativo (soft delete) definindo active=false.
     * Se não encontrar, lança UserNotFoundException.
     * Retorna mensagem de confirmação.
     */
    @Transactional
    public String deleteUser(Long id) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setActive(false);
        userRepository.save(user);
        logger.info("Usuário ID {} marcado como inativo", id);

        return "Usuário removido com sucesso.";
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles(),
                user.getCreationDateTime(),
                user.getUpdateDateTime());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com e-mail: " + email));
        return mapToResponse(user);
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.setActive(true);
        userRepository.save(user);
        logger.info("Usuário ID {} ativado manualmente.", userId);
    }

}

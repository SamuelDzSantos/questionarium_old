package br.com.questionarium.user_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
import br.com.questionarium.user_service.repository.UserRepository;
import br.com.questionarium.user_service.security.model.VerificationToken;
import br.com.questionarium.user_service.security.repository.VerificationTokenRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final RabbitTemplate rabbitTemplate;

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

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // 1) Verifica duplicidade de e-mail
        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new EmailAlreadyExistsException(request.getEmail());
                });

        // 2) Persiste user inactive
        User user = new User(request.getName(), request.getEmail(), request.getRoles());
        user.setActive(false);
        User saved = userRepository.save(user);
        logger.info("Usuário salvo com ID {}", saved.getId());

        // 3) Gera e salva token
        VerificationToken token = new VerificationToken(saved);
        tokenRepository.save(token);
        logger.info("Token gerado para usuário {}: {}", saved.getId(), token.getToken());

        // 4) Após commit, publica eventos
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // UserCreatedEvent
                Role role = Role.valueOf(request.getRoles().get(0));
                UserCreatedEvent event = new UserCreatedEvent(
                        saved.getId(), saved.getEmail(), request.getPassword(), role);
                try {
                    rabbitTemplate.convertAndSend(USER_EXCHANGE, ROUTING_KEY_CREATED, event);
                } catch (Exception ex) {
                    logger.error("Erro ao publicar UserCreatedEvent", ex);
                }
                // Email de confirmação
                String link = BASE_URL + "/users/confirm?token=" + token.getToken();
                Email mail = new Email(
                        "Confirme seu cadastro",
                        "Olá " + saved.getName() + ", clique: " + link,
                        saved.getEmail());
                try {
                    rabbitTemplate.convertAndSend(USER_EXCHANGE, ROUTING_KEY_EMAIL, mail);
                } catch (Exception ex) {
                    logger.error("Erro ao enviar email de confirmação", ex);
                }
            }
        });

        return mapToResponse(saved);
    }

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
        tokenRepository.deleteByUser_Id(user.getId());
        return "Usuário confirmado com sucesso.";
    }

    /** Lista apenas os usuários com active=true */
    @Transactional(readOnly = true)
    public List<UserResponse> listAllUsers() {
        return userRepository.findAllByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /** Lista **todos** os usuários, independente de active */
    @Transactional(readOnly = true)
    public List<UserResponse> listAllIncludingInactive() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + email));
        return mapToResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setName(request.getName());
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            user.setRoles(request.getRoles());
        }
        return mapToResponse(userRepository.save(user));
    }

    /**
     * Soft-delete: marca active=false para qualquer usuário (admin ou self).
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
        userRepository.save(user);
    }

    // ------------------------------------------------------------------------------------------------
    private UserResponse mapToResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getRoles(),
                u.getCreationDateTime(),
                u.getUpdateDateTime());
    }

}

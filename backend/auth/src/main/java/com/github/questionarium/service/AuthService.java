package com.github.questionarium.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.questionarium.config.exception.UserNotConfirmedException;
import com.github.questionarium.interfaces.DTOs.AuthDataDTO;
import com.github.questionarium.interfaces.DTOs.AuthUserRegisterDTO;
import com.github.questionarium.interfaces.DTOs.Email;
import com.github.questionarium.interfaces.DTOs.LoginFormDTO;
import com.github.questionarium.interfaces.DTOs.PasswordUpdateForm;
import com.github.questionarium.model.Token;
import com.github.questionarium.model.User;
import com.github.questionarium.repository.TokenRepository;
import com.github.questionarium.repository.UserRepository;
import com.github.questionarium.types.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RabbitTemplate rabbitTemplate;

    public String login(LoginFormDTO loginForm) {

        log.info("Tentativa de login para usuário {} com senha {}", loginForm.login(), loginForm.password());

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(
                loginForm.login(),
                loginForm.password());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException ex) {
            log.error("Credenciais inválidas para usuário: {}", loginForm.login());
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        User user = getUser(loginForm.login());
        if (user.getUses2Factor()) {
            log.info("Usuário utiliza 2factor");
            return null;
        }
        log.info("Usuário {} encontrado e logado!", user.getLogin());
        String token = jwtUtils.generateToken(user.getId(), loginForm.login(), user.getRole());
        log.info("Token: {} gerado!", token);
        return token;
    }

    public User register(AuthUserRegisterDTO register) {

        User user = userRepository.findByLogin(register.email()).orElse(null);

        System.out.println(user);

        // Caso usuário não exista cria um novo usuário
        if (user == null) {
            String encodedPassword = passwordEncoder.encode(register.password());
            user = new User(null, register.email(), encodedPassword, UserRole.USER, false, false);
            user = userRepository.save(user);
            log.info("Usuário de id {} email {} criado!", user.getId(), user.getLogin());

            // Caso o usuário exista mas não esteja ativo (Tentar acessar o cadastro antes
            // de ativar o usuário) retorna usuário já criado e segue SAGA normalmente.
        } else if (user.getActive().equals(false)) {
            return user;
        }
        // Caso não atenda nenhum dos dois acima retorna um erro de usuário não
        // encontrado.
        else {
            throw new UsernameNotFoundException("Usuário de login " + register.email() + " já existe!");
        }

        return user;

    }

    public Boolean updatePassword(Long userId, String currentPassword, String confirmPassword, String newPassword) {
        log.info("Atualizando senha do Usuário id: {}. Nova senha: {}", userId, newPassword);
        User user = getUser(userId);
        if (!newPassword.equals(confirmPassword)) {
            log.info("Senhas {} e {} não são iguais!", newPassword, confirmPassword);
            return false;
        }

        String password = user.getPassword();
        if (passwordEncoder.matches(currentPassword, password)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } else {
            log.info("Senha {} não é igual a senha cadastrada!", newPassword);
            return false;
        }

    }

    public String resetPassword(String email) {
        User user = getUser(email);
        Token token = new Token(null, user.getId(), generateRandomString(), LocalDateTime.now().plusMinutes(60));
        tokenRepository.save(token);
        String confirmationURL = "http://localhost:4200/recuperar-senha?token=" + token.getToken();
        Email mail = new Email(
                "Confirme seu cadastro",
                "Olá " + user.getLogin() + ", clique aqui para resetar o password: " + confirmationURL,
                user.getLogin());
        try {
            var a = rabbitTemplate.convertSendAndReceiveAsType(
                    // RabbitMQConfig.EXCHANGE,
                    "SEND_EMAIL_EVENT",
                    mail,
                    new ParameterizedTypeReference<Boolean>() {
                    });

        } catch (Exception ex) {
            log.error("Erro ao enviar email de confirmação", ex);
        }
        return confirmationURL;
    }

    public Boolean resetPasswordToken(String token, PasswordUpdateForm passwordUpdateForm) {
        Token tok = tokenRepository.findByToken(token).orElse(null);
        if (tok != null) {
            User user = getUser(tok.getUserId());
            if (passwordUpdateForm.newPassword().equals(passwordUpdateForm.confirmPassword())) {
                user.setPassword(passwordEncoder.encode(passwordUpdateForm.newPassword()));
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }

    public String generateConfirmationToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(null);
        Token token = new Token(null, user.getId(), generateRandomString(), LocalDateTime.now().plusMinutes(60));
        tokenRepository.save(token);
        // String confirmationURL = "http://localhost:14000/auth/token/" +
        // token.getToken();
        String confirmationURL = "http://localhost:4200/auth?token=" + token.getToken();
        System.out.println(confirmationURL);
        return confirmationURL;
    }

    public Boolean validateEmail(String tokenId) {
        Token token = tokenRepository.findByToken(tokenId).get();
        if (LocalDateTime.now().isAfter(token.getValidUntil())) {
            throw new RuntimeException("Token expirado!");
        }

        User user = userRepository.findById(token.getUserId()).get();
        user.setActive(true);
        userRepository.save(user);

        return true;
    }

    public AuthDataDTO getUserData(Long userId) {
        User user = getUser(userId);
        return new AuthDataDTO(user.getId(), user.getLogin(), user.getRole().toString());
    }

    private User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário de id " + userId + " não encontrado!"));

        if (!user.getActive()) {
            throw new UserNotConfirmedException();
        }

        return user;

    }

    private User getUser(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário de login " + login + " não encontrado!"));

        if (!user.getActive()) {
            throw new UserNotConfirmedException();
        }
        return user;
    }

    public Boolean userExists(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user == null ? false : true;
    }

    public void remove(Long userId) {
        log.info("Definindo status do usuário {} como false.", userId);
        User user = getUser(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    private String generateRandomString() {

        UUID uuid = UUID.randomUUID();
        String uuidToken = uuid.toString();
        return uuidToken;
    }

}

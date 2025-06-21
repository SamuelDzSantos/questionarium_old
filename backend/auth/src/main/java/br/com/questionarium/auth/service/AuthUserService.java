package br.com.questionarium.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import br.com.questionarium.auth.exception.UserNotConfirmedException;
import br.com.questionarium.auth.exception.ApiErrorException;
import br.com.questionarium.auth.interfaces.CreatedUserAuthDTO;
import br.com.questionarium.auth.interfaces.LoginFormDTO;
import br.com.questionarium.auth.model.AuthUser;
import br.com.questionarium.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {
    private final AuthUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtUtils;
    private final UserServiceClient userServiceClient;

    // Método para registro via RabbitMQ listener
    public void register(CreatedUserAuthDTO userData) {
        AuthUser user = AuthUser.builder()
                .login(userData.getLogin())
                .password(userData.getPassword())
                .role(userData.getRole())
                .build();
        userRepository.save(user);
        log.info("Usuário registrado via AuthListener: {}", user.getLogin());
    }

    public String login(LoginFormDTO loginForm) {
        log.info("Tentativa de login para usuário: {}", loginForm.getLogin());
        // 1) Autenticação
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginForm.getLogin(), loginForm.getPassword()));
        } catch (AuthenticationException ex) {
            log.error("Credenciais inválidas para usuário: {}", loginForm.getLogin());
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        // 2) Busca usuário no Mongo
        AuthUser authUser = userRepository.findByLogin(loginForm.getLogin())
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado."));

        // 3) Busca ID no User Service
        Long userIdPostgres;
        try {
            userIdPostgres = userServiceClient.getUserIdByEmail(authUser.getLogin());
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Usuário não confirmado: {}", authUser.getLogin());
            throw new UserNotConfirmedException();
        } catch (RestClientException e) {
            log.error("Erro de comunicação com UserService: {}", authUser.getLogin(), e);
            throw new ApiErrorException(HttpStatus.BAD_GATEWAY, "Serviço de usuários indisponível.");
        }

        // 4) Gera JWT
        String token = jwtUtils.generateToken(
                authUser.getId(),
                userIdPostgres,
                authUser.getLogin(),
                authUser.getRole());
        log.info("Login bem-sucedido: {}", authUser.getLogin());
        return token;
    }
}

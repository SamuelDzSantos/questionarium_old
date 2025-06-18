package br.com.questionarium.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import br.com.questionarium.auth.interfaces.CreatedUserAuthDTO;
import br.com.questionarium.auth.interfaces.LoginFormDTO;
import br.com.questionarium.auth.model.AuthUser;
import br.com.questionarium.auth.repository.AuthUserRepository;
import br.com.questionarium.types.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {
    private final AuthUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtUtils;
    private final UserServiceClient userServiceClient;

    public void register(CreatedUserAuthDTO userData) {
        try {
            AuthUser user = AuthUser.builder()
                    .login(userData.getLogin())
                    .password(passwordEncoder.encode(userData.getPassword()))
                    .role(userData.getRole())
                    .build();
            userRepository.save(user);
            log.info("Usuário criado com sucesso: {}", user.getLogin());
        } catch (Exception e) {
            log.error("Falha ao criar usuário: {}", userData.getLogin(), e);
            throw e;
        }
    }

    public String login(LoginFormDTO loginForm) {
        log.info("Tentativa de login para usuário: {}", loginForm.getLogin());
        try {
            AuthUser user = userRepository.findByLogin(loginForm.getLogin())
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Usuário não encontrado: " + loginForm.getLogin()));

            var authToken = new UsernamePasswordAuthenticationToken(
                    loginForm.getLogin(),
                    loginForm.getPassword());
            authenticationManager.authenticate(authToken);

            String mongoUserId = user.getId();
            String username = user.getLogin();
            Role role = user.getRole();

            Long userIdPostgres = userServiceClient.getUserIdByEmail(username);

            String token = jwtUtils.generateToken(
                    mongoUserId,
                    userIdPostgres,
                    username,
                    role);

            log.info("Login bem-sucedido para usuário: {}", username);
            return token;

        } catch (UsernameNotFoundException ex) {
            log.error("Falha na autenticação para usuário: {}", loginForm.getLogin(), ex);
            throw new BadCredentialsException("Credenciais inválidas para usuário: " + loginForm.getLogin());
        } catch (AuthenticationException ex) {
            log.error("Usuário não encontrado: {}", loginForm.getLogin(), ex);
            throw ex; // Será tratado pelo GlobalExceptionHandler
        } catch (RestClientException ex) {
            log.error("Erro de comunicação com UserService para usuário: {}", loginForm.getLogin(), ex);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Erro inesperado durante login para usuário: {}", loginForm.getLogin(), ex);
            throw ex;
        }
    }
}

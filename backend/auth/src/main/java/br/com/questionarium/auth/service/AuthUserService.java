package br.com.questionarium.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.questionarium.auth.interfaces.CreatedUserAuthDTO;
import br.com.questionarium.auth.interfaces.LoginFormDTO;
import br.com.questionarium.auth.model.AuthUser;
import br.com.questionarium.auth.repository.AuthUserRepository;
import br.com.questionarium.auth.service.other.JwtUtils;
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
    private final JwtUtils jwtUtils;

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
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + loginForm.getLogin()));

            var authToken = new UsernamePasswordAuthenticationToken(
                    loginForm.getLogin(),
                    loginForm.getPassword());
            authenticationManager.authenticate(authToken);

            String userId = user.getId();
            String username = user.getLogin();
            Role role = user.getRole();

            String token = jwtUtils.generateToken(userId, username, role);
            log.info("Login bem-sucedido para usuário: {}", username);
            return token;
        } catch (AuthenticationException ex) {
            log.error("Falha na autenticação para usuário: {}", loginForm.getLogin(), ex);
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Erro durante login para usuário: {}", loginForm.getLogin(), ex);
            throw ex;
        }
    }
}

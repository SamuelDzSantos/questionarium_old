package br.net.dac.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.net.dac.auth.interfaces.LoginFormDTO;
import br.net.dac.auth.model.AuthUser;
import br.net.dac.auth.repository.AuthUserRepository;
import br.net.dac.auth.service.other.JwtUtils;
import br.net.dac.entities.auth.CreatedUserAuthDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    private final AuthUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public void register(CreatedUserAuthDTO userData) {

        AuthUser user = userRepository
                .save(AuthUser.builder().login(userData.login()).password(passwordEncoder.encode(userData.password()))
                        .role(userData.role())
                        .build());
        System.out.println(user.getLogin());
    }

    public String login(LoginFormDTO loginForm) {

        userRepository.findByLogin(loginForm.login()).orElseThrow(() -> new RuntimeException());
        Authentication authenticationToken = UsernamePasswordAuthenticationToken
                .unauthenticated(loginForm.login(), loginForm.password());

        authenticationManager.authenticate(authenticationToken);

        String token = jwtUtils.generate(loginForm.login());

        return token;
    }

    public void removeUser(String email) {
        AuthUser user = userRepository.findByLogin(email).orElseThrow(() -> new RuntimeException());
        userRepository.delete(user);
    }

}

package dev.questionarium.services;

import org.springframework.stereotype.Service;

import dev.questionarium.model.AuthUser;
import dev.questionarium.repositories.AuthUserRepository;
import dev.questionarium.types.Role;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final JwtUtils jwtUtils;

    public Mono<String> login(String email, String password) {
        return this.authenticate(email, password).map((result) -> {
            if (result == true) {
                AuthUser user = authUserRepository.findByEmail(email).orElse(null);
                String token = jwtUtils.generate(user.getEmail(), user.getId());
                return token;
            } else {
                return "";
            }
        });
    }

    public void register(String email, String password) {
        long randomNum = (long) (Math.random() * 10001);
        AuthUser authUser = new AuthUser(randomNum, email, password, Role.USER);
        authUserRepository.save(authUser);
    }

    public Mono<Boolean> authenticate(String email, String password) {
        return Mono.just(authUserRepository.findByEmail(email).orElse(null) == null ? false : true);
    }

}

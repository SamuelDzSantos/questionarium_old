package dev.questionarium.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.questionarium.entities.AuthRequest;
import dev.questionarium.entities.AuthResponse;
import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.entities.UserData;
import dev.questionarium.model.User;
import dev.questionarium.repository.UserRepository;
import dev.questionarium.types.AccessType;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

        private final UserRepository userRepository;
        private final JwtUtils jwtUtils;
        private final PasswordEncoder encoder;

        public UserData save(RegistrationRequest userRegistration) {

                User user = User.builder().email(userRegistration.email()).id(null)
                                .name(userRegistration.name())
                                .password(encoder.encode(userRegistration.password()))
                                .roles(List.of(userRegistration.role())).build();

                user = this.userRepository.save(user);

                UserData userData = UserData.builder().name(user.getName()).email(user.getEmail()).id(user.getId())
                                .roles(user.getRoles().stream().map(role -> role.getAsString()).toList())
                                .build();

                return userData;
        }

        public UserData getUserByEmail(String email) {
                User user = getUser(email);
                return new UserData(user.getId(), user.getName(), user.getEmail(),
                                user.getRoles().stream().map(role -> role.getAsString()).toList());
        }

        public UserData getUserById(Long id) {
                User user = this.getUser(id);
                return new UserData(user.getId(), user.getName(), user.getEmail(),
                                user.getRoles().stream().map(role -> role.getAsString()).toList());
        }

        public AuthResponse login(AuthRequest authRequest) {

                User user = this.getUser(authRequest.login());
                if (encoder.matches(authRequest.password(), user.getPassword())) {

                        String accessToken = jwtUtils.generate(user.getEmail(), AccessType.ACCESS);
                        String refreshToken = jwtUtils.generate(user.getEmail(), AccessType.REFRESH);
                        UserData userData = new UserData(user.getId(), user.getName(), user.getEmail(),
                                        user.getRoles().stream().map(role -> role.getAsString()).toList());
                        return new AuthResponse(accessToken, refreshToken, userData);
                } else {
                        throw new RuntimeException("Senha incorreta!");
                }
        }

        private User getUser(Long id) {
                return this.userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        }

        private User getUser(String email) {
                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException(
                                                "Usuário de email: " + email + "não encontrado!"));
        }

}

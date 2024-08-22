package dev.questionarium.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.entities.UserData;
import dev.questionarium.model.User;
import dev.questionarium.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

        private final UserRepository userRepository;

        public UserData save(RegistrationRequest userRegistration) {
                // doto register user in database
                User user = User.builder().email(userRegistration.email()).id(null)
                                .password(userRegistration.password())
                                .role(userRegistration.role()).name(userRegistration.name()).build();

                user = this.userRepository.save(user);

                UserData userData = new UserData(user.getId(), user.getName(), user.getPassword(), user.getEmail(),
                                List.of(user.getRole().getAsString()));

                return userData;
        }

        public UserData getUserByEmail(String email) {
                User user = getUser(email);
                return new UserData(user.getId(), user.getName(), user.getPassword(), user.getEmail(),
                                List.of(user.getRole().getAsString()));
        }

        public UserData getUserById(Long id) {
                User user = this.getUser(id);
                return new UserData(user.getId(), user.getName(), user.getPassword(), user.getEmail(),
                                List.of(user.getRole().getAsString()));
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

package org.ufpr.questionarium.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ufpr.questionarium.dtos.RegisterForm;
import org.ufpr.questionarium.models.User;
import org.ufpr.questionarium.repositories.UserRepository;
import org.ufpr.questionarium.types.Role;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void register(RegisterForm registerForm) {
        if (registerForm.getPassword().equals(registerForm.getConfirmPassowrd())) {
            User user = new User(null, registerForm.getName(), registerForm.getEmail(), registerForm.getName(),
                    LocalDate.now(), List.of(Role.ROLE_TEACHER));
            user.setPassword(encoder.encode(user.getPassword()));
            this.userRepository.save(user);
        }
    }
}

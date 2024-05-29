package org.ufpr.questionarium.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ufpr.questionarium.dtos.LoggedUser;
import org.ufpr.questionarium.dtos.RegisterForm;
import org.ufpr.questionarium.dtos.UpdatedUserForm;
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
        User user = new User(null, registerForm.getName(), registerForm.getEmail(), registerForm.getPassword(),
                LocalDate.now(), List.of(Role.ROLE_TEACHER));
        user.setPassword(encoder.encode(user.getPassword()));
        this.userRepository.save(user).getName();
    }

    public void register(String username, String userEmail, String password) {
        User user = new User(null, username, userEmail, encoder.encode(password), LocalDate.now(),
                List.of(Role.ROLE_TEACHER));
        this.userRepository.save(user).getName();
    }

    public LoggedUser getUser(Authentication authentication) {
        return this.userRepository.findByEmail(authentication.getName())
                .map((user) -> new LoggedUser(user.getName(), user.getEmail()))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    public void updateUser(Authentication authentication, UpdatedUserForm form) {
        User user = this.userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new RuntimeException("Senhas não são iguais!");
        }
        user.setEmail(form.getEmail());
        user.setName(form.getName());
        user.setPassword(encoder.encode(form.getPassword()));
        this.userRepository.save(user);
    }

}
package dev.questionarium.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.questionarium.model.PasswordToken;
import dev.questionarium.model.User;
import dev.questionarium.repository.PasswordTokenRepository;
import dev.questionarium.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;

    public String forgotPassword(String email) {
        User user = this.getUser(email);
        String token = generateToken();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 86480 * 1000);
        this.passwordTokenRepository
                .save(PasswordToken.builder().expirationDate(expirationDate).id(user.getId()).token(token).build());
        return token;
    }

    public boolean resetPassword(String token, String password) {

        PasswordToken passwordToken = this.passwordTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token não encontrado!"));

        Long userId = passwordToken.getId();
        User user = this.getUser(userId);

        user.setPassword(password);

        this.userRepository.save(user);

        List<PasswordToken> tokens = this.passwordTokenRepository.findByUser(user);

        this.passwordTokenRepository.deleteAll(tokens);

        return true;
    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Usuário de email: " + email + "não encontrado!"));
    }

    private User getUser(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }
}

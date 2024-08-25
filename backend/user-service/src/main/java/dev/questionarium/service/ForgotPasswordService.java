package dev.questionarium.service;

import java.util.Date;
import java.util.List;
import java.util.Random;
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

    public void checkPasswordToken(String token) {

        this.passwordTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token não encontrado!"));

    }

    public String forgotPassword(String email) {

        User user = this.getUser(email);

        String token = generateToken();
        String code = generateCode();
        Date now = new Date();
        // 15 min -> 900000 milis
        Date expirationDate = new Date(now.getTime() + 900000);

        this.passwordTokenRepository
                .save(PasswordToken.builder()
                        .id(null)
                        .expirationDate(expirationDate)
                        .user(user)
                        .code(code)
                        .token(token)
                        .build());
        return token;
    }

    public void resetPassword(String token, String password, String code) {

        PasswordToken passwordToken = this.passwordTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token não encontrado!"));

        if (!passwordToken.getCode().equals(code))
            throw new RuntimeException("Código informado não é igual ao código do token!");

        Date now = new Date();

        if (passwordToken.getExpirationDate().before(now))
            throw new RuntimeException("Token expirado. Gere um novo token!");

        Long userId = passwordToken.getUser().getId();
        User user = this.getUser(userId);

        user.setPassword(password);

        this.userRepository.save(user);

        List<PasswordToken> tokens = this.passwordTokenRepository.findByUser(user);

        tokens.stream().forEach((entity) -> {
            this.passwordTokenRepository.deleteById(entity.getId());
        });

    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    private String generateCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
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

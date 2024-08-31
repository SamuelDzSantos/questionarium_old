package dev.questionarium.service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.questionarium.entities.Email;
import dev.questionarium.entities.PasswordPatch;
import dev.questionarium.entities.ResetPasswordValidation;
import dev.questionarium.exception.BadRequestException;
import dev.questionarium.exception.TokenNotFoundException;
import dev.questionarium.exception.UserNotFoundException;
import dev.questionarium.model.PasswordToken;
import dev.questionarium.model.User;
import dev.questionarium.producer.EmailProducer;
import dev.questionarium.repository.PasswordTokenRepository;
import dev.questionarium.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder encoder;
    private final EmailProducer producer;

    public String checkCodeValue(ResetPasswordValidation validation) {

        User user = getUser(validation.email());
        PasswordToken passwordToken = passwordTokenRepository.findByUserAndCode(user, validation.code())
                .orElseThrow(BadRequestException.getException("Código incorreto!"));

        checkTokenExpired(passwordToken);

        String token = generateToken();

        passwordToken.setToken(encoder.encode(token));

        passwordTokenRepository.save(passwordToken);

        return passwordToken.getToken();
    }

    public void forgotPassword(String email) {

        User user = this.getUser(email);

        String code = generateCode();
        Date now = new Date();

        Email emailMessage = Email.builder().message("O seu código é : " + code + "!")
                .emailTo(user.getEmail()).subject("Código de verificação").build();

        try {
            producer.sendEmail(emailMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } // 15 min -> 900000 milis
        Date expirationDate = new Date(now.getTime() + 900000);

        this.passwordTokenRepository
                .save(PasswordToken.builder()
                        .id(null)
                        .expirationDate(expirationDate)
                        .user(user)
                        .code(code)
                        .token(null)
                        .build());
    }

    public void resetPassword(PasswordPatch patch) {

        PasswordToken passwordToken = getToken(encoder.encode(patch.token()));

        checkTokenExpired(passwordToken);

        Long userId = passwordToken.getUser().getId();
        User user = this.getUser(userId);

        if (!patch.password().equals(patch.confirmPassword())) {
            throw new BadRequestException("Senhas não são iguais!");
        }

        user.setPassword(encoder.encode(patch.password()));

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

    private PasswordToken getToken(String token) {
        return this.passwordTokenRepository.findByToken(token)
                .orElseThrow(TokenNotFoundException.getException(token));
    }

    private User getUser(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(UserNotFoundException.getException(id));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException.getException(email));
    }

    private void checkTokenExpired(PasswordToken token) {
        Date now = new Date();

        if (token.getExpirationDate().before(now))
            throw new BadRequestException(
                    String.format("Token '%s' expirado. Gere um novo token!", token.getToken()));

    }
}

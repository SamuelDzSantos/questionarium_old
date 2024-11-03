package dev.questionarium.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.questionarium.model.PasswordToken;
import dev.questionarium.model.User;

import java.util.Optional;
import java.util.List;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    Optional<PasswordToken> findByToken(String token);

    List<PasswordToken> findByUser(User user);

    Optional<PasswordToken> findByUserAndCode(User user, String code);

}
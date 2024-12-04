package dev.questionarium.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.questionarium.model.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByEmail(String email);

}
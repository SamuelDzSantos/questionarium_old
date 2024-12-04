package dev.questionarium.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import dev.questionarium.model.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByEmail(String email);

}
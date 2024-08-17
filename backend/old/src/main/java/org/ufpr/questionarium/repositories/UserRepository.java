package org.ufpr.questionarium.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ufpr.questionarium.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
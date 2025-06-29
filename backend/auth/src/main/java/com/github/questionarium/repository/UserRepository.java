package com.github.questionarium.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.questionarium.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    Optional<User> findByLogin(String login);

}

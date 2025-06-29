package com.github.questionarium.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.questionarium.model.Token;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    Optional<Token> findByToken(String token);

}

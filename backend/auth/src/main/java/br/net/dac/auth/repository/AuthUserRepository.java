package br.net.dac.auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.net.dac.auth.model.AuthUser;

import java.util.List;

@Repository
public interface AuthUserRepository extends MongoRepository<AuthUser, String> {

    Optional<AuthUser> findByLogin(String login);

    List<AuthUser> findByPassword(String password);

}

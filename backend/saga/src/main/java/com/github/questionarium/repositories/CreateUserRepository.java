package com.github.questionarium.repositories;

import org.springframework.data.repository.CrudRepository;

import com.github.questionarium.model.CreateUserState;

public interface CreateUserRepository extends CrudRepository<CreateUserState, Long> {

}

package com.github.questionarium.repositories;

import org.springframework.data.repository.CrudRepository;

import com.github.questionarium.model.PatchUserState;

public interface PatchUserRepository extends CrudRepository<PatchUserState, Long> {

}

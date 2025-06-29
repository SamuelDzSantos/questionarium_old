package com.github.questionarium.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.questionarium.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

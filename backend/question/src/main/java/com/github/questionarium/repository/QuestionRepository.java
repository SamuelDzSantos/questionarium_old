package com.github.questionarium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.questionarium.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

}
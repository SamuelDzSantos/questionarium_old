package com.github.questionarium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.questionarium.model.AssessmentModel;

public interface AssessmentModelRepository extends JpaRepository<AssessmentModel, Long> {
    List<AssessmentModel> findByUserId(Long userId);
}

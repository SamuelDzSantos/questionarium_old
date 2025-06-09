package com.questionarium.assessment_service.repository;

import com.questionarium.assessment_service.model.AssessmentModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentModelRepository extends JpaRepository<AssessmentModel, Long> {
    List<AssessmentModel> findByUserId(Long userId);
}

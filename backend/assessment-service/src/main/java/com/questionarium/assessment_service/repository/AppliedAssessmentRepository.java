package com.questionarium.assessment_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.questionarium.assessment_service.model.AppliedAssessment;

public interface AppliedAssessmentRepository extends JpaRepository<AppliedAssessment, Long> {

    List<AppliedAssessment> findByUserId(Long userId);

    List<AppliedAssessment> findByActiveTrue();
    
    List<AppliedAssessment> findByUserIdAndActiveTrue(Long userId);
}

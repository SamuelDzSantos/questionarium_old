package com.questionarium.assessment_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.questionarium.assessment_service.model.AssessmentHeader;

public interface AssessmentHeaderRepository extends JpaRepository<AssessmentHeader, Long> {
    List<AssessmentHeader> findByUserId(Long userId);
}
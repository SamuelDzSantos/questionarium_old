package com.github.questionarium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.questionarium.model.AssessmentHeader;

public interface AssessmentHeaderRepository extends JpaRepository<AssessmentHeader, Long> {
    List<AssessmentHeader> findByUserId(Long userId);
}
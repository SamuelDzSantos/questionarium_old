package com.github.questionarium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.questionarium.model.AppliedAssessment;

public interface AppliedAssessmentRepository extends JpaRepository<AppliedAssessment, Long>, JpaSpecificationExecutor<AppliedAssessment> {

    List<AppliedAssessment> findByUserId(Long userId);

    List<AppliedAssessment> findByActiveTrue();

    List<AppliedAssessment> findByUserIdAndActiveTrue(Long userId);
}

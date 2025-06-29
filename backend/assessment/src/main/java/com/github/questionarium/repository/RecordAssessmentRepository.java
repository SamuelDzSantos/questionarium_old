package com.github.questionarium.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.questionarium.model.RecordAssessment;

@Repository
public interface RecordAssessmentRepository extends JpaRepository<RecordAssessment, Long> {

    List<RecordAssessment> findByAppliedAssessmentId(Long appliedAssessmentId);

    List<RecordAssessment> findByAppliedAssessmentIdAndActiveTrue(Long appliedAssessmentId);

    List<RecordAssessment> findByAppliedAssessmentUserId(Long userId);

    List<RecordAssessment> findByAppliedAssessmentUserIdAndActiveTrue(Long userId);

    List<RecordAssessment> findByActiveTrue();
}

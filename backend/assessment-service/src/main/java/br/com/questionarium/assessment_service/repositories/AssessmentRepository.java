package br.com.questionarium.assessment_service.repositories;

import br.com.questionarium.assessment_service.models.Assessment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findAllAssessmentsByUserId(Long userId);
}
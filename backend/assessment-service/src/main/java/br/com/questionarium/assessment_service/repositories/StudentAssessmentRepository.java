package br.com.questionarium.assessment_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.questionarium.assessment_service.models.StudentAssessment;

@Repository
public interface StudentAssessmentRepository extends JpaRepository<StudentAssessment, Long>  {

}

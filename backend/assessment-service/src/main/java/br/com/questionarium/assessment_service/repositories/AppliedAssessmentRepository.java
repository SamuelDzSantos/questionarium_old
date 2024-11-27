package br.com.questionarium.assessment_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.questionarium.assessment_service.models.AppliedAssessment;
import java.util.List;

@Repository
public interface AppliedAssessmentRepository extends JpaRepository<AppliedAssessment, Long> {

    List<AppliedAssessment> findAllApliedAssessmentsByUserId(Long userId);

}

package br.com.questionarium.assessment_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.questionarium.assessment_service.models.ApliedAssessment;
import java.util.List;

@Repository
public interface ApliedAssessmentRepository extends JpaRepository<ApliedAssessment, Long> {

    List<ApliedAssessment> findAllApliedAssessmentsByUserId(Long userId);

}

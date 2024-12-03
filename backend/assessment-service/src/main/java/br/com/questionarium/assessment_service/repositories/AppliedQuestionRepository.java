package br.com.questionarium.assessment_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.questionarium.assessment_service.models.AppliedQuestion;

@Repository
public interface AppliedQuestionRepository extends JpaRepository<AppliedQuestion, Long>  {

    List<AppliedQuestion> findByAppliedAssessmentId(Long appliedAssessmentId);

}

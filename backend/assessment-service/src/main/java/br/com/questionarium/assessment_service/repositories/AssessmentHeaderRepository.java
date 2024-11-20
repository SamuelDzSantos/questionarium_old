package br.com.questionarium.assessment_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.questionarium.assessment_service.models.AssessmentHeader;

@Repository
public interface AssessmentHeaderRepository extends JpaRepository<AssessmentHeader, Long> {
    // List<AssessmentHeader> findByInstituicao(String instituicao); CASO PRECISE BUSCAR POR INSTITUICOES
    List<AssessmentHeader> findAllHeadersByUserId(Long userId);
}
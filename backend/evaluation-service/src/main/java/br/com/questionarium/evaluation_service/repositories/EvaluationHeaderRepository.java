package br.com.questionarium.evaluation_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.questionarium.evaluation_service.models.EvaluationHeader;

@Repository
public interface EvaluationHeaderRepository extends JpaRepository<EvaluationHeader, Long> {
    // List<EvaluationHeader> findByInstituicao(String instituicao); CASO PRECISE BUSCAR POR INSTITUICOES
    List<EvaluationHeader> findAllByUserId(Long userId);
}
package br.com.questionarium.evaluation_service.repositories;

import br.com.questionarium.evaluation_service.models.Evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findAllEvaluationsByUserId(Long userId);
}
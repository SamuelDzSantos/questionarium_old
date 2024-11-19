package br.com.questionarium.evaluation_service.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.questionarium.evaluation_service.models.Evaluation;
import br.com.questionarium.evaluation_service.repositories.EvaluationRepository;

@Service
public class EvaluationService {
    @Autowired
    private EvaluationRepository evaluationRepository;

    // CRIA AVALIACAO
    public Evaluation createEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    // PEGA TODAS AS AVALIACOES
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    // PEGA TODAS AVALIACOES DE 1 USER
    public List<Evaluation> getEvaluationsByUserId(Long userId) {
        return evaluationRepository.findAllEvaluationsByUserId(userId);
    }

    // DELETA AVALIACAO
    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }

    // ADD QUESTAO NO FINAL DA LISTA DE QUESTOES DE UMA AVALIACAO
    public Evaluation addQuestion(Long evaluationId, Long questionId) {
        Optional<Evaluation> evaluationOpt = evaluationRepository.findById(evaluationId);
        if (evaluationOpt.isPresent()) {
            Evaluation evaluation = evaluationOpt.get();
            evaluation.getQuestions().add(questionId);
            return evaluationRepository.save(evaluation);
        }
        throw new RuntimeException("Avaliação não encontrada");
    }

    // REMOVE QUESTAO DA LISTA DE QUESTOES DE UMA AVALIACAO
    public Evaluation removeQuestion(Long evaluationId, Long questionId) {
        Optional<Evaluation> evaluationOpt = evaluationRepository.findById(evaluationId);
        if (evaluationOpt.isPresent()) {
            Evaluation evaluation = evaluationOpt.get();
            evaluation.getQuestions().remove(questionId);
            return evaluationRepository.save(evaluation);
        }
        throw new RuntimeException("Avaliação não encontrada");
    }

    // ALTERA O CABECALHO DE UMA AVALIACAO
    public Evaluation updateHeaderId(Long evaluationId, Long newHeaderId) {
        Optional<Evaluation> evaluationOpt = evaluationRepository.findById(evaluationId);
        if (evaluationOpt.isPresent()) {
            Evaluation evaluation = evaluationOpt.get();
            evaluation.setHeaderId(newHeaderId);
            return evaluationRepository.save(evaluation);
        }
        throw new RuntimeException("Avaliação não encontrada");
    }
}

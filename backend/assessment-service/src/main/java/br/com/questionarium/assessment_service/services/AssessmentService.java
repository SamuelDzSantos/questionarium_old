package br.com.questionarium.assessment_service.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.questionarium.assessment_service.models.Assessment;
import br.com.questionarium.assessment_service.repositories.AssessmentRepository;

@Service
public class AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;

    // CRIA AVALIACAO
    public Assessment createAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    // PEGA TODAS AS AVALIACOES
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    // PEGA UMA AVALIACAO POR ID
    public Assessment getAssessmentById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com o ID: " + id));
    }

    // PEGA TODAS AVALIACOES DE 1 USER
    public List<Assessment> getAllAssessmentsByUserId(Long userId) {
        return assessmentRepository.findAllAssessmentsByUserId(userId);
    }

    // DELETA AVALIACAO
    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);
    }

    // ALTERA ORDEM QUESTOES NA LISTA
    public Assessment reorderQuestions(Long id, List<Long> newQuestionsOrder) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));

        assessment.setQuestions(newQuestionsOrder);
        return assessmentRepository.save(assessment);
    }

    // ALTERA O CABECALHO DE UMA AVALIACAO
    public Assessment updateHeaderId(Long assessmentId, Long newHeaderId) {
        Optional<Assessment> assessmentOpt = assessmentRepository.findById(assessmentId);
        if (assessmentOpt.isPresent()) {
            Assessment assessment = assessmentOpt.get();
            assessment.setHeaderId(newHeaderId);
            return assessmentRepository.save(assessment);
        }
        throw new RuntimeException("Avaliação não encontrada");
    }

}

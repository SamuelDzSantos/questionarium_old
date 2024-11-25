package br.com.questionarium.assessment_service.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.questionarium.assessment_service.models.ApliedAssessment;
import br.com.questionarium.assessment_service.models.Assessment;
import br.com.questionarium.assessment_service.repositories.ApliedAssessmentRepository;
import br.com.questionarium.assessment_service.repositories.AssessmentRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApliedAssessmentService {

    @Autowired
    private ApliedAssessmentRepository apliedAssessmentRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    public ApliedAssessmentService(ApliedAssessmentRepository apliedAssessmentRepository,
            AssessmentRepository assessmentRepository) {
        this.apliedAssessmentRepository = apliedAssessmentRepository;
        this.assessmentRepository = assessmentRepository;
    }

    // CRIAR AVALIACAO APLICADA
    public ApliedAssessment createApliedAssessment(Long assessmentId, int quantity, LocalDate applicationDate) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found with ID: " + assessmentId));

        ApliedAssessment apliedAssessment = new ApliedAssessment();
        apliedAssessment.setQuestions(assessment.getQuestions());
        apliedAssessment.setAnswerKey(assessment.getAnswerKey());
        apliedAssessment.setApplicationDate(applicationDate);
        apliedAssessment.setQuantity(quantity);
        apliedAssessment.setUserId(assessment.getUserId());
        apliedAssessment.setHeaderId(assessment.getHeaderId());
        apliedAssessment.setStatus(true); // Ativo por padrão

        return apliedAssessmentRepository.save(apliedAssessment);
    }

    // BUSCAR 1 AVALIACAO APLICADA POR ID
    public ApliedAssessment getApliedAssessmentById(Long id) {
        return apliedAssessmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ApliedAssessment not found with ID: " + id));
    }

    // BUSCA TODAS AVALIACOES APLICADAS DE UM USER
    public List<ApliedAssessment> getAllApliedAssessmentsByUserId(Long userId) {
        return apliedAssessmentRepository.findAllApliedAssessmentsByUserId(userId);
    }

}

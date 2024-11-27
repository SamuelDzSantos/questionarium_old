package br.com.questionarium.assessment_service.services;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.questionarium.assessment_service.models.AppliedAssessment;
import br.com.questionarium.assessment_service.models.Assessment;
import br.com.questionarium.assessment_service.repositories.AppliedAssessmentRepository;
import br.com.questionarium.assessment_service.repositories.AssessmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppliedAssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AppliedAssessmentRepository appliedAssessmentRepository;

    public AppliedAssessment createAppliedAssessment(Long assessmentId, int quantity, LocalDate applicationDate) {
        // Busca o Assessment pelo ID
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma avaliacao encontrada com o id: " + assessmentId));
                
        // Cria o AppliedAssessment
        AppliedAssessment appliedAssessment = new AppliedAssessment();
        // appliedAssessment.setQuestions(assessment.getQuestions());
        appliedAssessment.setQuestions(assessment.getQuestions().stream().toList());

        // appliedAssessment.setAnswerKey(assessment.getAnswerKey());
        appliedAssessment.setAnswerKey(assessment.getAnswerKey().entrySet().stream().collect(Collectors.toMap(q -> q.getKey(), q -> q.getValue())));
        appliedAssessment.setCreationDate(LocalDate.now());
        appliedAssessment.setUserId(assessment.getUserId());
        appliedAssessment.setHeaderId(assessment.getHeaderId());
        appliedAssessment.setApplicationDate(applicationDate);
        appliedAssessment.setQuantity(quantity);
        appliedAssessment.setStatus(true); // Ativado por padrão

        // Salva e retorna o AppliedAssessment
        return appliedAssessmentRepository.save(appliedAssessment);
    }

}

package br.com.questionarium.assessment_service.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.questionarium.assessment_service.models.ApliedAssessment;
import br.com.questionarium.assessment_service.models.Assessment;
import br.com.questionarium.assessment_service.repositories.ApliedAssessmentRepository;
import br.com.questionarium.assessment_service.repositories.AssessmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApliedAssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ApliedAssessmentRepository apliedAssessmentRepository;

    public ApliedAssessment createApliedAssessment(Long assessmentId, int quantity, LocalDate applicationDate) {
        // Busca o Assessment pelo ID
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma avaliacao encontrada com o id: " + assessmentId));
                
        // Cria o ApliedAssessment
        ApliedAssessment apliedAssessment = new ApliedAssessment();
        // apliedAssessment.setQuestions(assessment.getQuestions());
        apliedAssessment.setQuestions(assessment.getQuestions().stream().toList());

        // apliedAssessment.setAnswerKey(assessment.getAnswerKey());
        apliedAssessment.setAnswerKey(assessment.getAnswerKey().entrySet().stream().collect(Collectors.toMap(q -> q.getKey(), q -> q.getValue())));
        apliedAssessment.setCreationDate(LocalDate.now());
        apliedAssessment.setUserId(assessment.getUserId());
        apliedAssessment.setHeaderId(assessment.getHeaderId());
        apliedAssessment.setApplicationDate(applicationDate);
        apliedAssessment.setQuantity(quantity);
        apliedAssessment.setStatus(true); // Ativado por padrão

        // Salva e retorna o ApliedAssessment
        return apliedAssessmentRepository.save(apliedAssessment);
    }

}

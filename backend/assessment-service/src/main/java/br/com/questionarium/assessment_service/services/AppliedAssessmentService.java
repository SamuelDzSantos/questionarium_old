package br.com.questionarium.assessment_service.services;

import java.time.LocalDate;
import java.util.List;
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

    @Autowired
    private AppliedHeaderService appliedHeaderService;

    // CRIAR AVALIACAO APLICADA
    public AppliedAssessment createAppliedAssessment(Long assessmentId, int quantity, LocalDate applicationDate) {
        // Busca o Assessment pelo ID
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Nenhuma avaliacao encontrada com o id: " + assessmentId));

        // Cria o AppliedAssessment
        AppliedAssessment appliedAssessment = new AppliedAssessment();
        // appliedAssessment.setQuestions(assessment.getQuestions());
        appliedAssessment.setQuestions(assessment.getQuestions().stream().toList());

        // appliedAssessment.setAnswerKey(assessment.getAnswerKey());
        appliedAssessment.setAnswerKey(assessment.getAnswerKey().entrySet().stream()
                .collect(Collectors.toMap(q -> q.getKey(), q -> q.getValue())));
        appliedAssessment.setCreationDate(LocalDate.now());
        appliedAssessment.setUserId(assessment.getUserId());
        appliedAssessment.setHeaderId(assessment.getHeaderId());
        appliedAssessment.setApplicationDate(applicationDate);
        appliedAssessment.setQuantity(quantity);
        appliedAssessment.setStatus(true); // Ativado por padrão

        // Cria o AppliedHeader
        appliedHeaderService.createApliedHeader(appliedAssessment.getHeaderId(), appliedAssessment.getUserId());

        // Salva e retorna o AppliedAssessment
        return appliedAssessmentRepository.save(appliedAssessment);
    }

    // LISTAR TODAS AS AVALIACOES APLICADAS
    public List<AppliedAssessment> getAllAppliedAssessments() {
        return appliedAssessmentRepository.findAll();
    }

    // BUSCAR UMA AVALIACAO APLICADA POR ID
    public AppliedAssessment getAppliedAssessmentById(Long id) {
        return appliedAssessmentRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Nenhuma avaliacao aplicada encontrada com o id: " + id));
    }

    // BUSCAR TODAS AS AVALIACOES APLICADAS DE UM USUARIO ATIVAS
    public List<AppliedAssessment> getAllActiveAppliedAssessmentsByUserId(Long userId) {
        return appliedAssessmentRepository.findAllByUserIdAndStatus(userId, true);
    }

    // LISTAR TODAS AS AVALIACOES APLICADAS DE UM USUARIO
    public List<AppliedAssessment> getAllAppliedAssessmentsByUserId(Long userId) {
        return appliedAssessmentRepository.findAllAppliedAssessmentsByUserId(userId);
    }

    // DESATIVA AVALIACAO APLICADA
    public AppliedAssessment disableAppliedAssessment(Long id) {
        AppliedAssessment appliedAssessment = appliedAssessmentRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Nenhuma avaliacao aplicada encontrada com o id: " + id));
        appliedAssessment.setStatus(false);
        return appliedAssessmentRepository.save(appliedAssessment);
    }

}

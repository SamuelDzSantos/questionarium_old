package br.com.questionarium.assessment_service.services;

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

    public ApliedAssessmentService(ApliedAssessmentRepository apliedAssessmentRepository,
            AssessmentRepository assessmentRepository) {
        this.apliedAssessmentRepository = apliedAssessmentRepository;
    }

    // CRIAR AVALIACAO APLICADA
    public ApliedAssessment createApliedAssessment(Assessment assessment, ApliedAssessment request) {
        ApliedAssessment newAppliedAssessment = new ApliedAssessment();

        newAppliedAssessment.setQuestions(assessment.getQuestions());
        newAppliedAssessment.setAnswerKey(assessment.getAnswerKey());
        newAppliedAssessment.setApplicationDate(request.getApplicationDate());
        newAppliedAssessment.setQuantity(request.getQuantity());
        newAppliedAssessment.setUserId(assessment.getUserId());
        newAppliedAssessment.setHeaderId(assessment.getHeaderId());
        newAppliedAssessment.setStatus(true); // Status padrão como ativo

        return apliedAssessmentRepository.save(newAppliedAssessment);
    }

    // BUSCAR 1 AVALIACAO APLICADA POR ID
    public ApliedAssessment getApliedAssessmentById(Long apliedAssessmentId) {
        return apliedAssessmentRepository.findById(apliedAssessmentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("ApliedAssessment not found with ID: " + apliedAssessmentId));
    }

    // BUSCA TODAS AVALIACOES APLICADAS DE UM USER
    public List<ApliedAssessment> getAllApliedAssessmentsByUserId(Long userId) {
        return apliedAssessmentRepository.findAllApliedAssessmentsByUserId(userId);
    }

    // // ALTERAR STATUS PARA FALSE (DESATIVAR A AVALIAÇÃO)
    // public void deleteApliedAssessmentById(Long apliedAssessmentId) {
    //     ApliedAssessment apliedAssessment = apliedAssessmentRepository.findById(apliedAssessmentId)
    //             .orElseThrow(
    //                     () -> new EntityNotFoundException("ApliedAssessment not found with ID: " + apliedAssessmentId));

    //     apliedAssessment.setStatus(false);
    //     apliedAssessmentRepository.save(apliedAssessment);
    // }

    // ATUALIZA O STATUS DE UMA APLICACAO DE AVALIACAO
    public void updateStatus(Long apliedAssessmentId, Boolean status) {
        ApliedAssessment apliedAssessment = apliedAssessmentRepository.findById(apliedAssessmentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("ApliedAssessment not found with ID: " + apliedAssessmentId));

        apliedAssessment.setStatus(status);
        apliedAssessmentRepository.save(apliedAssessment);
    }

}

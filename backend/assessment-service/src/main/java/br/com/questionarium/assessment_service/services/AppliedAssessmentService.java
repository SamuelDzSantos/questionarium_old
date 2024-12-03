package br.com.questionarium.assessment_service.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.questionarium.assessment_service.models.AppliedAssessment;
import br.com.questionarium.assessment_service.models.Assessment;
import br.com.questionarium.assessment_service.models.AssessmentHeader;
import br.com.questionarium.assessment_service.repositories.AppliedAssessmentRepository;
import br.com.questionarium.assessment_service.repositories.AssessmentHeaderRepository;
import br.com.questionarium.assessment_service.repositories.AssessmentRepository;

@Service
public class AppliedAssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentHeaderRepository assessmentHeaderRepository;

    @Autowired
    private AppliedAssessmentRepository appliedAssessmentRepository;

    public void createAppliedAssessment(Long id, int quantity, boolean shuffle, LocalDate applicationDate) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliacao nao encontrada"));

        AssessmentHeader header = assessmentHeaderRepository.findById(assessment.getHeaderId())
                .orElseThrow(() -> new RuntimeException("Header nao encontrado"));

        AppliedAssessment appliedAssessment = new AppliedAssessment();
        appliedAssessment.setUserId(assessment.getUserId());
        appliedAssessment.setOriginalAssessmentId(id);
        appliedAssessment.setInstitution(header.getInstitution());
        appliedAssessment.setDepartment(header.getDepartment());
        appliedAssessment.setCourse(header.getCourse());
        appliedAssessment.setClassroom(header.getClassroom());
        appliedAssessment.setProfessor(header.getProfessor());
        appliedAssessment.setInstructions(header.getInstructions());
        appliedAssessment.setImage(header.getImage());
        appliedAssessment.setCreationDate(LocalDate.now());
        appliedAssessment.setApplicationDate(applicationDate);
        appliedAssessment.setQuantity(quantity);
        appliedAssessment.setStatus(true);
        appliedAssessment.setShuffle(shuffle);

        appliedAssessmentRepository.save(appliedAssessment);

        //CHAMADA SAGA PARA SALVAR AS QUESTOES

    }


}

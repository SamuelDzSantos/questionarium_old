package br.com.questionarium.assessment_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.questionarium.assessment_service.models.AppliedHeader;
import br.com.questionarium.assessment_service.models.AssessmentHeader;
import br.com.questionarium.assessment_service.repositories.AppliedHeaderRepository;
import br.com.questionarium.assessment_service.repositories.AssessmentHeaderRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ApliedHeaderService {

    @Autowired
    private AppliedHeaderRepository apliedHeaderRepository;

    @Autowired
    private AssessmentHeaderRepository assessmentHeaderRepository;

    public ApliedHeaderService(AppliedHeaderRepository apliedHeaderRepository,
            AssessmentHeaderRepository assessmentHeaderRepository) {
        this.apliedHeaderRepository = apliedHeaderRepository;
        this.assessmentHeaderRepository = assessmentHeaderRepository;
    }

    // CRIAR CABECALHO APLICADO
    public AppliedHeader createApliedHeader(Long headerId, Long userId) {

        AssessmentHeader originalHeader = assessmentHeaderRepository.findById(headerId)
                .orElseThrow(() -> new EntityNotFoundException("Cabecalho nao encontrado com o id: " + headerId));

        AppliedHeader apliedHeader = new AppliedHeader();
        apliedHeader.setInstitution(originalHeader.getInstitution());
        apliedHeader.setDepartment(originalHeader.getDepartment());
        apliedHeader.setCourse(originalHeader.getCourse());
        apliedHeader.setClassroom(originalHeader.getClassroom());
        apliedHeader.setProfessor(originalHeader.getProfessor());
        apliedHeader.setInstructions(originalHeader.getInstructions());
        apliedHeader.setImage(originalHeader.getImage());
        apliedHeader.setOriginalHeaderId(originalHeader.getId());
        apliedHeader.setUserId(userId);

        return apliedHeaderRepository.save(apliedHeader);
    }

    // BUSCAR 1 CABECALHO APLICADO POR ID
    public AppliedHeader getApliedHeaderById(Long apliedHeaderId) {
        return apliedHeaderRepository.findById(apliedHeaderId)
                .orElseThrow(() -> new EntityNotFoundException("Cabecalho nao encontrado com o id: " + apliedHeaderId));
    }
}

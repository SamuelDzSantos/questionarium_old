package com.questionarium.assessment_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.questionarium.assessment_service.model.AssessmentHeader;
import com.questionarium.assessment_service.repository.AssessmentHeaderRepository;

@Service
public class AssessmentHeaderService {

    @Autowired
    private AssessmentHeaderRepository assessmentHeaderRepository;

    // CRIA HEADER
    public AssessmentHeader createHeader(AssessmentHeader header) {
        return assessmentHeaderRepository.save(header);
    }

    // BUSCA HEADER POR ID
    public Optional<AssessmentHeader> getHeaderById(Long id) {
        return assessmentHeaderRepository.findById(id);
    }

    // BUSCA HEADERS POR USER ID
    public List<AssessmentHeader> getHeadersByUserId(Long userId) {
        return assessmentHeaderRepository.findByUserId(userId);
    }

    // BUSCA TODOS OS HEADERS
    public List<AssessmentHeader> getAllHeaders() {
        return assessmentHeaderRepository.findAll();
    }

    // ATUALIZA HEADER
    public Optional<AssessmentHeader> updateHeader(Long id, AssessmentHeader updatedHeader) {
        return assessmentHeaderRepository.findById(id).map(existingHeader -> {
            existingHeader.setInstitution(updatedHeader.getInstitution());
            existingHeader.setDepartment(updatedHeader.getDepartment());
            existingHeader.setCourse(updatedHeader.getCourse());
            existingHeader.setClassroom(updatedHeader.getClassroom());
            existingHeader.setProfessor(updatedHeader.getProfessor());
            existingHeader.setInstructions(updatedHeader.getInstructions());
            existingHeader.setImage(updatedHeader.getImage());
            return assessmentHeaderRepository.save(existingHeader);
        });
    }

    // DELETA HEADER
    public void deleteHeader(Long id) {
        assessmentHeaderRepository.deleteById(id);
    }
}

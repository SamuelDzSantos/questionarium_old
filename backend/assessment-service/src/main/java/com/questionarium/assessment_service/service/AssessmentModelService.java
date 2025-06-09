package com.questionarium.assessment_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.repository.AssessmentModelRepository;

import jakarta.transaction.Transactional;

@Service
public class AssessmentModelService {
    @Autowired
    private AssessmentModelRepository assessmentModelRepository;

    // CRIA AVALIACAO
    public AssessmentModel createAssessment(AssessmentModel assessment) {
        return assessmentModelRepository.save(assessment);
    }

    // BUSCA AVALIACAO POR ID
    public Optional<AssessmentModel> getAssessmentById(Long id) {
        return assessmentModelRepository.findById(id);
    }

    // BUSCA TODAS AS AVALIACOES - NIVEL ADMIN
    public List<AssessmentModel> getAllAssessments() {
        return assessmentModelRepository.findAll();
    }

    // BUSCA AVALIACOES POR ID DO USUARIO
    public List<AssessmentModel> getAssessmentsByUserId(Long userId) {
        return assessmentModelRepository.findByUserId(userId);
    }

    // ATUALIZA AVALIACAO
    @Transactional
    public Optional<AssessmentModel> updateAssessment(Long id, AssessmentModel updatedAssessment) {
        return assessmentModelRepository.findById(id).map(existing -> {
            existing.setDescription(updatedAssessment.getDescription());
            existing.setQuestions(updatedAssessment.getQuestions());
            existing.setUserId(updatedAssessment.getUserId());
            existing.setInstitution(updatedAssessment.getInstitution());
            existing.setDepartment(updatedAssessment.getDepartment());
            existing.setCourse(updatedAssessment.getCourse());
            existing.setClassroom(updatedAssessment.getClassroom());
            existing.setProfessor(updatedAssessment.getProfessor());
            existing.setInstructions(updatedAssessment.getInstructions());
            existing.setImage(updatedAssessment.getImage());
            return assessmentModelRepository.save(existing);
        });
    }

    // DELETA AVALIACAO
    public boolean deleteAssessment(Long id) {
        if (assessmentModelRepository.existsById(id)) {
            assessmentModelRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
package com.questionarium.assessment_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.repository.AssessmentModelRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AssessmentModelService {

    private final AssessmentModelRepository assessmentModelRepository;

    /** Cria um novo modelo de avaliação */
    public AssessmentModel createAssessment(AssessmentModel assessment) {
        log.info("Criando novo AssessmentModel para usuário {}", assessment.getUserId());
        return assessmentModelRepository.save(assessment);
    }

    /** Busca um modelo por ID ou lança 404 */
    @Transactional(readOnly = true)
    public AssessmentModel getAssessmentById(Long id) {
        log.info("Buscando AssessmentModel com id {}", id);
        return assessmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Modelo de avaliação não encontrado: " + id));
    }

    /** Busca todos os modelos (admin) */
    @Transactional(readOnly = true)
    public List<AssessmentModel> getAllAssessments() {
        log.info("Buscando todos os AssessmentModels");
        return assessmentModelRepository.findAll();
    }

    /** Busca modelos por userId */
    @Transactional(readOnly = true)
    public List<AssessmentModel> getAssessmentsByUserId(Long userId) {
        log.info("Buscando AssessmentModels do usuário {}", userId);
        return assessmentModelRepository.findByUserId(userId);
    }

    /** Atualiza um modelo existente */
    public AssessmentModel updateAssessment(Long id, AssessmentModel updatedAssessment) {
        log.info("Atualizando AssessmentModel com id {}", id);

        AssessmentModel existing = assessmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Modelo de avaliação não encontrado para atualização: " + id));

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

        AssessmentModel saved = assessmentModelRepository.save(existing);
        log.info("AssessmentModel {} atualizado com sucesso", saved.getId());
        return saved;
    }

    /** Deleta um modelo */
    public void deleteAssessment(Long id) {
        log.info("Deletando AssessmentModel com id {}", id);

        if (!assessmentModelRepository.existsById(id)) {
            throw new BusinessException("Modelo de avaliação não encontrado para exclusão: " + id);
        }

        assessmentModelRepository.deleteById(id);
        log.info("AssessmentModel {} deletado com sucesso", id);
    }
}

package com.questionarium.assessment_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.repository.AssessmentModelRepository;
import com.questionarium.assessment_service.security.JwtTokenDecoder;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AssessmentModelService {

    private final AssessmentModelRepository assessmentModelRepository;
    private final JwtTokenDecoder jwtUtils;

    /** Cria um novo modelo de avaliação */
    public AssessmentModel createAssessment(AssessmentModel assessment) {
        Long currentUserId = jwtUtils.getCurrentUserId();
        assessment.setUserId(currentUserId);
        log.info("Criando novo AssessmentModel para usuário {}", currentUserId);
        return assessmentModelRepository.save(assessment);
    }

    /** Busca um modelo por ID ou lança 404, com controle de acesso */
    @Transactional(readOnly = true)
    public AssessmentModel getAssessmentById(Long id) {
        log.info("Buscando AssessmentModel com id {}", id);
        AssessmentModel existing = assessmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Modelo de avaliação não encontrado: " + id));
        if (!isAdmin() && !existing.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("Você não tem permissão para acessar este modelo");
        }
        return existing;
    }

    /** Busca todos os modelos (apenas admin) */
    @Transactional(readOnly = true)
    public List<AssessmentModel> getAllAssessments() {
        if (!isAdmin()) {
            throw new BusinessException("Somente administradores podem listar todos os modelos");
        }
        log.info("ADMIN: buscando todos os AssessmentModels");
        return assessmentModelRepository.findAll();
    }

    /** Busca modelos por userId (admin ou próprio usuário) */
    @Transactional(readOnly = true)
    public List<AssessmentModel> getAssessmentsByUserId(Long userId) {
        log.info("Buscando AssessmentModels do usuário {}", userId);
        if (!isAdmin() && !userId.equals(getCurrentUserId())) {
            throw new BusinessException("Você não tem permissão para listar modelos de outro usuário");
        }
        return assessmentModelRepository.findByUserId(userId);
    }

    /** Atualiza um modelo existente */
    public AssessmentModel updateAssessment(Long id, AssessmentModel updatedAssessment) {
        log.info("Atualizando AssessmentModel com id {}", id);
        AssessmentModel existing = assessmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Modelo de avaliação não encontrado para atualização: " + id));
        if (!isAdmin() && !existing.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("Você não tem permissão para atualizar este modelo");
        }
        existing.setDescription(updatedAssessment.getDescription());
        existing.setQuestions(updatedAssessment.getQuestions());
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

    /** Deleta um modelo (admin ou próprio usuário) */
    public void deleteAssessment(Long id) {
        log.info("Deletando AssessmentModel com id {}", id);
        AssessmentModel existing = assessmentModelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Modelo de avaliação não encontrado para exclusão: " + id));
        if (!isAdmin() && !existing.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("Você não tem permissão para excluir este modelo");
        }
        assessmentModelRepository.deleteById(id);
        log.info("AssessmentModel {} deletado com sucesso", id);
    }

    private Long getCurrentUserId() {
        return jwtUtils.getCurrentUserId();
    }

    private boolean isAdmin() {
        return jwtUtils.isAdmin();
    }
}

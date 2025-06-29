package com.github.questionarium.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.questionarium.client.QuestionClient;
import com.github.questionarium.config.exception.BusinessException;
import com.github.questionarium.interfaces.DTOs.RpcQuestionDTO;
import com.github.questionarium.model.AssessmentModel;
import com.github.questionarium.model.QuestionWeight;
import com.github.questionarium.repository.AssessmentModelRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AssessmentModelService {

    private final AssessmentModelRepository assessmentModelRepository;
    private final QuestionClient questionClient;

    /** Cria um novo modelo de avaliação com validação via RabbitMQ */
    public AssessmentModel createAssessment(AssessmentModel assessment, Long userId) {

        Long currentUserId = userId;

        List<Long> ids = assessment.getQuestions()
                .stream()
                .map(QuestionWeight::getQuestionId)
                .collect(Collectors.toList());
        Set<Long> uniqueIds = new HashSet<>();
        List<Long> duplicates = ids.stream()
                .filter(id -> !uniqueIds.add(id))
                .distinct()
                .collect(Collectors.toList());
        if (!duplicates.isEmpty()) {
            log.error("Tentativa de criar um modelo de avaliação com questões duplicadas: {}", duplicates);
            throw new BusinessException(
                    "Não é permitido repetir a mesma questão: duplicatas encontradas " + duplicates);
        }

        log.info("Criando novo modelo de avaliação para usuário {}", currentUserId);

        System.out.println(assessment.getQuestions());

        // Validação de cada questão
        for (QuestionWeight qw : assessment.getQuestions()) {
            Long qid = qw.getQuestionId();
            RpcQuestionDTO question = questionClient.getQuestion(qid);

            if (question == null) {
                throw new BusinessException("Questão não encontrada: id=" + qid);
            }
            if (Boolean.FALSE.equals(question.getEnable())) {
                throw new BusinessException("Questão não habilitada: id=" + qid);
            }

            // validação fina: PRIVATE só para quem criou
            boolean isPublic = "PUBLIC".equalsIgnoreCase(question.getAccessLevel());
            if (!isPublic && !question.getUserId().equals(currentUserId)) {
                throw new BusinessException(
                        "Você não tem permissão para usar a questão privada id=" + qid);
            }
        }

        // tudo validado: atribui dono e salva
        assessment.setUserId(currentUserId);
        return assessmentModelRepository.save(assessment);
    }

    /** Busca um modelo por ID ou lança 404, com controle de acesso */
    @Transactional(readOnly = true)
    public AssessmentModel getAssessmentById(Long id, Long userId, Boolean isAdmin) {
        log.info("Buscando modelo de avaliação com id {}", id);
        AssessmentModel existing = assessmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Modelo de avaliação não encontrado: " + id));
        if (!isAdmin && !existing.getUserId().equals(userId)) {
            throw new BusinessException("Você não tem permissão para acessar este modelo");
        }
        return existing;
    }

    /** Busca todos os modelos (apenas admin) */
    @Transactional(readOnly = true)
    public List<AssessmentModel> getAllAssessments(Boolean isAdmin) {
        if (!isAdmin) {
            throw new BusinessException("Somente administradores podem listar todos os modelos");
        }
        log.info("ADMIN: buscando todos os modelo de avaliação");
        return assessmentModelRepository.findAll();
    }

    /** Busca modelos por userId (admin ou próprio usuário) */
    @Transactional(readOnly = true)
    public List<AssessmentModel> getAssessmentsByUserId(Long userId, Boolean isAdmin) {
        log.info("Buscando modelo de avaliação do usuário {}", userId);
        if (!isAdmin && !userId.equals(userId)) {
            throw new BusinessException("Você não tem permissão para listar modelos de outro usuário");
        }
        return assessmentModelRepository.findByUserId(userId);
    }

    /** Atualiza um modelo existente */
    public AssessmentModel updateAssessment(Long id, AssessmentModel updatedAssessment, Long userId, Boolean isAdmin) {
        Long currentUserId = userId;

        log.info("Atualizando o modelo de avaliação de id {}", id);

        AssessmentModel existing = assessmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Modelo de avaliação não encontrado para atualização: " + id));

        if (!isAdmin && !existing.getUserId().equals(userId)) {
            throw new BusinessException("Você não tem permissão para atualizar este modelo");
        }

        List<Long> ids = updatedAssessment.getQuestions()
                .stream()
                .map(QuestionWeight::getQuestionId)
                .collect(Collectors.toList());

        Set<Long> uniqueIds = new HashSet<>();
        List<Long> duplicates = ids.stream()
                .filter(qid -> !uniqueIds.add(qid))
                .distinct()
                .collect(Collectors.toList());
        if (!duplicates.isEmpty()) {
            log.error("Tentativa de atualizar AssessmentModel {} com questões duplicadas: {}", id, duplicates);
            throw new BusinessException(
                    "Não é permitido repetir a mesma questão: duplicatas encontradas " + duplicates);
        }

        for (QuestionWeight questionWeight : updatedAssessment.getQuestions()) {
            Long questionId = questionWeight.getQuestionId();
            RpcQuestionDTO question = questionClient.getQuestion(questionId);
            if (question == null) {
                throw new BusinessException("Questão não encontrada: id=" + questionId);
            }
            if (Boolean.FALSE.equals(question.getEnable())) {
                throw new BusinessException("Questão não habilitada: id=" + questionId);
            }
            boolean isPublic = "PUBLIC".equalsIgnoreCase(question.getAccessLevel());
            if (!isPublic && !question.getUserId().equals(currentUserId)) {
                throw new BusinessException(
                        "Você não tem permissão para usar a questão privada id=" + questionId);
            }
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
        log.info("Modelo de avaliação {} atualizado com sucesso", saved.getId());
        return saved;
    }

    /** Deleta um modelo (admin ou próprio usuário) */
    public void deleteAssessment(Long id, Long userId, Boolean isAdmin) {
        log.info("Deletando modelo de avaliação com id {}", id);
        AssessmentModel existing = assessmentModelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Modelo de avaliação não encontrado para exclusão: " + id));
        if (!isAdmin && !existing.getUserId().equals(userId)) {
            throw new BusinessException("Você não tem permissão para excluir este modelo");
        }
        assessmentModelRepository.deleteById(id);
        log.info("Modelo de avaliação {} deletado com sucesso", id);
    }

}

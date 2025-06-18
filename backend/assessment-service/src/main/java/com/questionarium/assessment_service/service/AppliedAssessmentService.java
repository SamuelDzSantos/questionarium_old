package com.questionarium.assessment_service.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.client.QuestionClient;
import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.repository.AppliedAssessmentRepository;
import com.questionarium.assessment_service.repository.AssessmentModelRepository;
import com.questionarium.assessment_service.security.JwtTokenDecoder;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

import br.com.questionarium.dtos.RpcAlternativeDTO;
import br.com.questionarium.dtos.RpcAnswerKeyDTO;
import br.com.questionarium.dtos.RpcQuestionDTO;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class AppliedAssessmentService {

        private final AppliedAssessmentRepository appliedRepo;
        private final AssessmentModelRepository modelRepo;
        private final JwtTokenDecoder jwtUtils;

        private final QuestionClient questionClient;

        public AppliedAssessmentService(
                        AppliedAssessmentRepository appliedRepo,
                        AssessmentModelRepository modelRepo,
                        JwtTokenDecoder jwtUtils,
                        @Qualifier("questionClientRabbitMQ") QuestionClient questionClient) {
                this.appliedRepo = appliedRepo;
                this.modelRepo = modelRepo;
                this.jwtUtils = jwtUtils;
                this.questionClient = questionClient;
        }

        @Transactional(readOnly = true)
        public List<AppliedAssessment> findAllActive() {
                if (isAdmin()) {
                        return appliedRepo.findByActiveTrue();
                } else {
                        return appliedRepo.findByUserIdAndActiveTrue(getCurrentUserId());
                }
        }

        @Transactional(readOnly = true)
        public AppliedAssessment findById(Long id) {
                var applied = appliedRepo.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Avaliação aplicada não encontrada: " + id));
                if (!isAdmin() && (!applied.getUserId().equals(getCurrentUserId()) || !applied.getActive())) {
                        throw new BusinessException("Você não tem permissão para acessar esta avaliação");
                }
                return applied;
        }

        @Transactional(readOnly = true)
        public List<AppliedAssessment> findByUser(Long userId) {
                if (isAdmin()) {
                        return appliedRepo.findByUserId(userId);
                } else if (userId.equals(getCurrentUserId())) {
                        return appliedRepo.findByUserIdAndActiveTrue(userId);
                } else {
                        throw new BusinessException("Você não tem permissão para acessar avaliações de outro usuário");
                }
        }

        public AppliedAssessment applyAssessment(
                        Long modelId,
                        LocalDate applicationDate,
                        Integer quantity,
                        Boolean shuffleQuestions) {

                if (quantity == null || quantity <= 0) {
                        throw new BusinessException("A quantidade de aplicações deve ser um número positivo");
                }

                AssessmentModel model = modelRepo.findById(modelId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Modelo de avaliação não encontrado: " + modelId));

                List<QuestionSnapshot> snapshots = model.getQuestions().stream()
                                .map(qw -> {
                                        RpcQuestionDTO qDto = questionClient.getQuestion(qw.getQuestionId());
                                        if (qDto == null) {
                                                throw new BusinessException("Questão não encontrada para o ID: "
                                                                + qw.getQuestionId());
                                        }
                                        List<RpcAlternativeDTO> alts = questionClient
                                                        .getAlternatives(qw.getQuestionId());
                                        if (alts == null || alts.isEmpty()) {
                                                throw new BusinessException("Questão " + qw.getQuestionId()
                                                                + " não possui alternativas");
                                        }

                                        List<AlternativeSnapshot> altSnaps = alts.stream()
                                                        .map(a -> AlternativeSnapshot.builder()
                                                                        .alternative(a.getAlternative())
                                                                        .description(a.getDescription())
                                                                        .imagePath(a.getImagePath())
                                                                        .isCorrect(a.getIsCorrect())
                                                                        .explanation(a.getExplanation())
                                                                        .position(a.getPosition())
                                                                        .build())
                                                        .collect(Collectors.toList());

                                        return QuestionSnapshot.builder()
                                                        .question(qDto.getQuestion())
                                                        .multipleChoice(qDto.getMultipleChoice())
                                                        .numberLines(qDto.getNumberLines())
                                                        .educationLevel(qDto.getEducationLevel())
                                                        .header(qDto.getHeader())
                                                        .headerImage(qDto.getHeaderImage())
                                                        .answerId(qDto.getAnswerId())
                                                        .enable(qDto.getEnable())
                                                        .accessLevel(qDto.getAccessLevel())
                                                        .weight(qw.getWeight())
                                                        .tags(qDto.getTags())
                                                        .alternatives(altSnaps)
                                                        .build();
                                })
                                .collect(Collectors.toList());

                if (Boolean.TRUE.equals(shuffleQuestions)) {
                        Collections.shuffle(snapshots);
                }

                String correctKey = questionClient.getAnswerKeys(
                                snapshots.stream()
                                                .map(QuestionSnapshot::getQuestion)
                                                .collect(Collectors.toList()))
                                .stream()
                                .map(RpcAnswerKeyDTO::getAnswerKey)
                                .collect(Collectors.joining(",", "[", "]"));

                AppliedAssessment applied = new AppliedAssessment();
                applied.setDescription(model.getDescription());
                applied.setQuestionSnapshots(snapshots);
                applied.setTotalScore(snapshots.stream().mapToDouble(QuestionSnapshot::getWeight).sum());
                applied.setUserId(model.getUserId());
                applied.setInstitution(model.getInstitution());
                applied.setDepartment(model.getDepartment());
                applied.setCourse(model.getCourse());
                applied.setClassroom(model.getClassroom());
                applied.setProfessor(model.getProfessor());
                applied.setInstructions(model.getInstructions());
                applied.setImage(model.getImage());
                applied.setApplicationDate(applicationDate != null ? applicationDate : LocalDate.now());
                applied.setQuantity(quantity);
                applied.setShuffleQuestions(shuffleQuestions);
                applied.setActive(true);
                applied.setCorrectAnswerKey(correctKey);

                snapshots.forEach(snap -> snap.setAppliedAssessment(applied));

                return appliedRepo.save(applied);
        }

        public void softDelete(Long id) {
                var applied = findById(id);
                if (!isAdmin() && !applied.getActive()) {
                        throw new BusinessException("Esta avaliação já está inativa");
                }
                applied.setActive(false);
                appliedRepo.save(applied);
        }

        private Long getCurrentUserId() {
                return jwtUtils.getCurrentUserId();
        }

        private boolean isAdmin() {
                return jwtUtils.isAdmin();
        }
}

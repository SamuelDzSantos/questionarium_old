package com.questionarium.assessment_service.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.questionarium.assessment_service.client.QuestionClient;
import com.questionarium.assessment_service.dto.AnswerKeyDTO;
import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.repository.AppliedAssessmentRepository;
import com.questionarium.assessment_service.repository.AssessmentModelRepository;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AppliedAssessmentService {

        private final AppliedAssessmentRepository appliedRepo;
        private final AssessmentModelRepository modelRepo;
        private final QuestionClient questionClient;

        /** Lista todas as AppliedAssessment ativas (admin ou user só vê as dele) */
        @Transactional(readOnly = true)
        public List<AppliedAssessment> findAllActive() {
                if (isAdmin()) {
                        log.info("ADMIN: buscando todas as avaliações aplicadas ativas");
                        return appliedRepo.findByActiveTrue();
                } else {
                        Long currentUserId = getCurrentUserId();
                        log.info("USER: buscando avaliações aplicadas ativas do usuário {}", currentUserId);
                        return appliedRepo.findByUserIdAndActiveTrue(currentUserId);
                }
        }

        /** Busca uma AppliedAssessment por id com controle de acesso */
        @Transactional(readOnly = true)
        public AppliedAssessment findById(Long id) {
                log.info("Buscando AppliedAssessment com id {}", id);

                AppliedAssessment applied = appliedRepo.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Avaliação aplicada não encontrada: " + id));

                if (isAdmin()) {
                        log.info("ADMIN: acesso permitido à avaliação {}", id);
                        return applied;
                } else {
                        Long currentUserId = getCurrentUserId();
                        if (!applied.getUserId().equals(currentUserId)) {
                                throw new BusinessException("Você não tem permissão para acessar esta avaliação");
                        }
                        if (!applied.getActive()) {
                                throw new BusinessException("Esta avaliação está inativa e não pode ser acessada");
                        }
                        log.info("USER: acesso permitido à avaliação {}", id);
                        return applied;
                }
        }

        /** Lista avaliações aplicadas do usuário (somente ativas) */
        @Transactional(readOnly = true)
        public List<AppliedAssessment> findByUser(Long userId) {
                if (isAdmin()) {
                        log.info("ADMIN: buscando avaliações aplicadas do usuário {}", userId);
                        return appliedRepo.findByUserId(userId);
                } else {
                        Long currentUserId = getCurrentUserId();
                        if (!currentUserId.equals(userId)) {
                                throw new BusinessException(
                                                "Você não tem permissão para acessar avaliações de outro usuário");
                        }
                        log.info("USER: buscando avaliações aplicadas ativas do usuário {}", currentUserId);
                        return appliedRepo.findByUserIdAndActiveTrue(currentUserId);
                }
        }

        /** Aplica um template de AssessmentModel */
        public AppliedAssessment applyAssessment(
                        Long modelId,
                        LocalDate applicationDate,
                        Integer quantity,
                        Boolean shuffleQuestions) {

                if (quantity == null || quantity <= 0) {
                        throw new BusinessException("A quantidade de aplicações deve ser um número positivo");
                }

                log.info("Iniciando aplicação da avaliação do modelo {}, quantidade {}, data {}, embaralhar {}",
                                modelId, quantity, applicationDate, shuffleQuestions);

                AssessmentModel model = modelRepo.findById(modelId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Modelo de avaliação não encontrado: " + modelId));

                List<QuestionSnapshot> snapshots = model.getQuestions().stream()
                                .map(qw -> {
                                        var qDto = questionClient.getQuestion(qw.getQuestionId());
                                        if (qDto == null) {
                                                throw new BusinessException("Questão não encontrada para o ID: "
                                                                + qw.getQuestionId());
                                        }

                                        var alts = questionClient.getAlternatives(qw.getQuestionId());
                                        if (alts == null || alts.isEmpty()) {
                                                throw new BusinessException("A questão " + qw.getQuestionId()
                                                                + " não possui alternativas cadastradas");
                                        }

                                        QuestionSnapshot snap = new QuestionSnapshot();
                                        snap.setId(qDto.getId());
                                        snap.setMultipleChoice(qDto.getMultipleChoice());
                                        snap.setNumberLines(qDto.getNumberLines());
                                        snap.setEducationLevel(qDto.getEducationLevel());
                                        snap.setPersonId(qDto.getPersonId());
                                        snap.setHeader(qDto.getHeader());
                                        snap.setHeaderImage(qDto.getHeaderImage());
                                        snap.setAnswerId(qDto.getAnswerId());
                                        snap.setEnable(qDto.getEnable());
                                        snap.setAccessLevel(qDto.getAccessLevel());
                                        snap.setTags(qDto.getTags());
                                        snap.setWeight(qw.getWeight());

                                        List<AlternativeSnapshot> altSnaps = alts.stream()
                                                        .map(a -> {
                                                                AlternativeSnapshot as = new AlternativeSnapshot();
                                                                as.setId(a.getId());
                                                                as.setDescription(a.getDescription());
                                                                as.setImagePath(a.getImagePath());
                                                                as.setIsCorrect(a.getIsCorrect());
                                                                as.setExplanation(a.getExplanation());
                                                                as.setOrder(a.getOrder());
                                                                return as;
                                                        })
                                                        .collect(Collectors.toList());
                                        snap.setAlternatives(altSnaps);

                                        return snap;
                                })
                                .collect(Collectors.toList());

                if (Boolean.TRUE.equals(shuffleQuestions)) {
                        Collections.shuffle(snapshots);
                }

                List<Long> ids = snapshots.stream()
                                .map(QuestionSnapshot::getId)
                                .collect(Collectors.toList());
                List<AnswerKeyDTO> keys = questionClient.getAnswerKeys(ids);
                if (keys == null || keys.isEmpty()) {
                        throw new BusinessException("Não foi possível obter os gabaritos das questões");
                }
                String correctKey = keys.stream()
                                .map(AnswerKeyDTO::getAnswerKey)
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

                AppliedAssessment saved = appliedRepo.save(applied);
                log.info("Avaliação aplicada {} salva com sucesso", saved.getId());
                return saved;
        }

        /** Soft-delete com controle de acesso */
        public void softDelete(Long id) {
                log.info("Iniciando soft-delete da AppliedAssessment com id {}", id);

                AppliedAssessment applied = appliedRepo.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Avaliação aplicada não encontrada: " + id));

                if (isAdmin()) {
                        log.info("ADMIN: soft-delete autorizado para avaliação {}", id);
                } else {
                        Long currentUserId = getCurrentUserId();
                        if (!applied.getUserId().equals(currentUserId)) {
                                throw new BusinessException("Você não tem permissão para excluir esta avaliação");
                        }
                        if (!applied.getActive()) {
                                throw new BusinessException("Esta avaliação já está inativa");
                        }
                        log.info("USER: soft-delete autorizado para avaliação {}", id);
                }

                applied.setActive(false);
                appliedRepo.save(applied);
                log.info("Avaliação aplicada {} marcada como inativa", id);
        }

        /**
         * Helper: retorna ID do usuário atual (assumindo que você extrai no
         * SecurityContext)
         */
        private Long getCurrentUserId() {
                // Simulação: você precisará implementar com seu JWT ou UserDetails
                // Exemplo com string do JWT:
                // return
                // Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
                throw new UnsupportedOperationException("Implementar método getCurrentUserId()");
        }

        /** Helper: verifica se o usuário atual é ADMIN */
        private boolean isAdmin() {
                return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        }
}

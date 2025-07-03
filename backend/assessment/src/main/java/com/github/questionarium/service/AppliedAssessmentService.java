package com.github.questionarium.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.questionarium.client.QuestionClient;
import com.github.questionarium.config.exception.BusinessException;
import com.github.questionarium.interfaces.DTOs.RpcAlternativeDTO;
import com.github.questionarium.interfaces.DTOs.RpcAnswerKeyDTO;
import com.github.questionarium.interfaces.DTOs.RpcQuestionDTO;
import com.github.questionarium.model.AlternativeSnapshot;
import com.github.questionarium.model.AppliedAssessment;
import com.github.questionarium.model.AssessmentModel;
import com.github.questionarium.model.QuestionSnapshot;
import com.github.questionarium.repository.AppliedAssessmentRepository;
import com.github.questionarium.repository.AssessmentModelRepository;

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
        private final RecordAssessmentService recordService;
        private final QuestionClient questionClient;

        public AppliedAssessment applyAssessment(
                        Long modelId,
                        String description,
                        LocalDate applicationDate,
                        Integer quantity,
                        Boolean shuffleQuestions, Long loggedUserId, Boolean isAdmin) {

                // 1) valida quantidade
                if (quantity == null || quantity <= 0) {
                        throw new BusinessException("A quantidade de aplicações deve ser um número positivo");
                }

                // 2) busca modelo
                AssessmentModel model = modelRepo.findById(modelId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Modelo de avaliação não encontrado: " + modelId));
                // 3) monta snapshots de perguntas + alternativas
                List<QuestionSnapshot> snapshots = model.getQuestions().stream()
                                .map(qw -> {

                                        Long qid = qw.getQuestionId();

                                        RpcQuestionDTO qDto = questionClient.getQuestion(qid);

                                        log.info("QUestão:\n", qDto);
                                        if (qDto == null) {
                                                throw new BusinessException("Questão não encontrada para o ID: " + qid);
                                        }
                                        List<RpcAlternativeDTO> alts = questionClient.getAlternatives(qid);
                                        System.out.println(alts);
                                        if (alts == null || alts.isEmpty()) {
                                                throw new BusinessException(
                                                                "Questão " + qid + " não possui alternativas");
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

                // 4) opcionalmente embaralha as questões
                if (Boolean.TRUE.equals(shuffleQuestions)) {
                        Collections.shuffle(snapshots);
                }

                // 5) monta gabarito concatenado

                List<Long> questionIds = snapshots.stream()
                                .map(QuestionSnapshot::getQuestion)
                                .collect(Collectors.toList());
                List<RpcAnswerKeyDTO> answerKeys = questionClient.getAnswerKeys(questionIds, loggedUserId);

                if (answerKeys == null || answerKeys.isEmpty()) {
                        throw new BusinessException(
                                        "Não foi possível obter o gabarito das questões: " + questionIds);
                }
                String correctKey = answerKeys.stream()
                                .map(RpcAnswerKeyDTO::getAnswerKey)
                                .collect(Collectors.joining(",", "[", "]"));

                // 6) popula entidade AppliedAssessment
                AppliedAssessment applied = new AppliedAssessment();
                applied.setDescription(description);
                applied.setQuestionSnapshots(snapshots);
                applied.setTotalScore(snapshots.stream()
                                .mapToDouble(QuestionSnapshot::getWeight).sum());
                applied.setUserId(model.getUserId());
                applied.setInstitution(model.getInstitution());
                applied.setDepartment(model.getDepartment());
                applied.setCourse(model.getCourse());
                applied.setClassroom(model.getClassroom());
                applied.setProfessor(model.getProfessor());
                applied.setInstructions(model.getInstructions());
                applied.setImage(model.getImage());
                applied.setApplicationDate(
                                applicationDate != null ? applicationDate : LocalDate.now());
                applied.setQuantity(quantity);
                applied.setShuffleQuestions(shuffleQuestions);
                applied.setActive(true);
                applied.setCorrectAnswerKey(correctKey);

                // 7) relaciona snapshots com o applied
                snapshots.forEach(snap -> snap.setAppliedAssessment(applied));

                // 8) salva o AppliedAssessment
                AppliedAssessment saved = appliedRepo.save(applied);

                // 9) cria automaticamente os RecordAssessment em branco
                List<String> emptyNames = Collections.nCopies(saved.getQuantity(), "");
                recordService.createFromAppliedAssessment(saved.getId(), emptyNames, loggedUserId, isAdmin);

                // 10) retorna o objeto salvo
                return saved;
        }

        @Transactional(readOnly = true)
        public List<AppliedAssessment> findAllActive(Long loggedUserId, Boolean isAdmin) {
                if (isAdmin) {
                        return appliedRepo.findByActiveTrue();
                } else {
                        return appliedRepo.findByUserIdAndActiveTrue(loggedUserId);
                }
        }

        @Transactional(readOnly = true)
        public AppliedAssessment findById(Long id, Long loggedUserId, Boolean isAdmin) {
                AppliedAssessment applied = appliedRepo.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Avaliação aplicada não encontrada: " + id));
                if (!isAdmin && (!applied.getUserId().equals(loggedUserId) || !applied.getActive())) {
                        throw new BusinessException("Você não tem permissão para acessar esta avaliação");
                }
                return applied;
        }

        @Transactional(readOnly = true)
        public List<AppliedAssessment> findByUser(Long userId, Long loggedUserId, Boolean isAdmin) {
                if (isAdmin) {
                        return appliedRepo.findByUserId(userId);
                } else if (userId.equals(loggedUserId)) {
                        return appliedRepo.findByUserIdAndActiveTrue(userId);
                } else {
                        throw new BusinessException("Você não tem permissão para acessar avaliações de outro usuário");
                }
        }

        public void softDelete(Long id, Long loggedUserId, Boolean isAdmin) {
                AppliedAssessment applied = findById(id, loggedUserId, isAdmin);
                if (!isAdmin && !applied.getActive()) {
                        throw new BusinessException("Esta avaliação já está inativa");
                }
                applied.setActive(false);
                appliedRepo.save(applied);
        }

        public List<AppliedAssessment> getFilteredAppliedAssessments(
                        Long userId,
                        Boolean isAdmin,
                        String description,
                        LocalDate applicationDate) {

                Specification<AppliedAssessment> spec;

                log.info("Buscando aplicações filtradas para userId={}, isAdmin={}", userId, isAdmin);

                spec = (root, query, cb) -> cb.isTrue(root.get("active"));

                if (!Boolean.TRUE.equals(isAdmin)) {
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("userId"), userId));
                }

                if (description != null && !description.trim().isEmpty()) {
                        spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("description")),
                                        "%" + description.toLowerCase() + "%"));
                }

                if (applicationDate != null) {
                        spec = spec.and((root, query, cb) -> cb.equal(root.get("applicationDate"), applicationDate));
                }

                return appliedRepo.findAll(spec);
        }

}

package com.questionarium.assessment_service.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.model.RecordAssessment;
import com.questionarium.assessment_service.repository.RecordAssessmentRepository;
import com.questionarium.assessment_service.security.JwtUtils;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RecordAssessmentService {

    private final RecordAssessmentRepository repository;
    private final AppliedAssessmentService appliedService;
    private final JwtUtils jwtUtils;

    /** Gera registros a partir da AppliedAssessment */
    public List<RecordAssessment> createFromAppliedAssessment(Long appliedAssessmentId, List<String> studentNames) {
        AppliedAssessment applied = appliedService.findById(appliedAssessmentId);
        List<QuestionSnapshot> baseSnapshots = applied.getQuestionSnapshots();
        int quantity = applied.getQuantity();

        if (studentNames.size() != quantity) {
            throw new BusinessException("Esperado " + quantity + " nomes, mas recebeu " + studentNames.size());
        }

        List<RecordAssessment> created = new ArrayList<>(quantity);

        for (int i = 0; i < quantity; i++) {
            List<QuestionSnapshot> snaps = baseSnapshots.stream()
                    .map(this::cloneSnapshot)
                    .collect(Collectors.toList());

            if (Boolean.TRUE.equals(applied.getShuffleQuestions())) {
                Collections.shuffle(snaps);
            }

            List<Long> questionOrder = snaps.stream()
                    .map(QuestionSnapshot::getId)
                    .collect(Collectors.toList());

            String correctKey = computeCorrectKey(snaps);

            RecordAssessment rec = new RecordAssessment();
            rec.setAppliedAssessment(applied);
            rec.setInstanceIndex(i + 1);
            rec.setStudentName(studentNames.get(i));
            rec.setQuestionOrder(questionOrder);
            rec.setQuestionSnapshots(snaps);
            rec.setTotalScore(applied.getTotalScore());
            rec.setCorrectAnswerKey(correctKey);

            created.add(repository.save(rec));
        }

        return created;
    }

    /** Cria um único registro (caso necessário) */
    public RecordAssessment create(RecordAssessment entity) {
        return repository.save(entity);
    }

    /** Busca por ID com controle de acesso */
    @Transactional(readOnly = true)
    public RecordAssessment findById(Long id) {
        RecordAssessment rec = repository.findById(id)
                .filter(RecordAssessment::getActive)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro de avaliação não encontrado ou inativo: " + id));

        if (isAdmin()) {
            log.info("ADMIN: acesso permitido à RecordAssessment {}", id);
            return rec;
        } else {
            Long currentUserId = getCurrentUserId();
            if (!rec.getAppliedAssessment().getUserId().equals(currentUserId)) {
                throw new BusinessException("Você não tem permissão para acessar este registro de avaliação");
            }
            log.info("USER: acesso permitido à RecordAssessment {}", id);
            return rec;
        }
    }

    /** Lista todas as ativas (USER → só as próprias, ADMIN → todas) */
    @Transactional(readOnly = true)
    public List<RecordAssessment> findAllActive() {
        if (isAdmin()) {
            log.info("ADMIN: buscando todas as RecordAssessment ativas");
            return repository.findByActiveTrue();
        } else {
            Long currentUserId = getCurrentUserId();
            log.info("USER: buscando RecordAssessment ativas do usuário {}", currentUserId);
            return repository.findByAppliedAssessmentUserIdAndActiveTrue(currentUserId);
        }
    }

    /** Busca por usuário (ADMIN → todas, USER → apenas as próprias ativas) */
    @Transactional(readOnly = true)
    public List<RecordAssessment> findByUser(Long userId) {
        if (isAdmin()) {
            log.info("ADMIN: buscando RecordAssessment do usuário {}", userId);
            return repository.findByAppliedAssessmentUserId(userId);
        } else {
            Long currentUserId = getCurrentUserId();
            if (!currentUserId.equals(userId)) {
                throw new BusinessException("Você não tem permissão para acessar registros de outro usuário");
            }
            log.info("USER: buscando RecordAssessment ativas do próprio usuário {}", currentUserId);
            return repository.findByAppliedAssessmentUserIdAndActiveTrue(currentUserId);
        }
    }

    /** Busca todas de uma AppliedAssessment */
    @Transactional(readOnly = true)
    public List<RecordAssessment> findByAppliedAssessment(Long appliedId) {
        AppliedAssessment applied = appliedService.findById(appliedId);
        if (isAdmin() || applied.getUserId().equals(getCurrentUserId())) {
            return repository.findByAppliedAssessmentIdAndActiveTrue(appliedId);
        } else {
            throw new BusinessException("Você não tem permissão para acessar registros desta avaliação");
        }
    }

    /** Atualiza apenas obtainedScore */
    public RecordAssessment updateObtainedScore(Long id, Double obtainedScore) {
        RecordAssessment rec = findById(id);
        rec.setObtainedScore(obtainedScore);
        return repository.save(rec);
    }

    /** Atualiza apenas studentAnswerKey */
    public RecordAssessment updateStudentAnswerKey(Long id, String studentAnswerKey) {
        RecordAssessment rec = findById(id);
        rec.setStudentAnswerKey(studentAnswerKey);
        return repository.save(rec);
    }

    /** Soft-delete (USER → só as próprias) */
    public void softDelete(Long id) {
        RecordAssessment rec = repository.findById(id)
                .filter(RecordAssessment::getActive)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro de avaliação não encontrado ou inativo: " + id));

        if (isAdmin()) {
            throw new BusinessException("Soft-delete de registros deve ser feito com método de admin específico");
        }

        Long currentUserId = getCurrentUserId();
        if (!rec.getAppliedAssessment().getUserId().equals(currentUserId)) {
            throw new BusinessException("Você não tem permissão para excluir este registro de avaliação");
        }

        rec.setActive(false);
        repository.save(rec);
        log.info("RecordAssessment {} marcado como inativo pelo USER", id);
    }

    /** Soft-delete ADMIN → qualquer registro */
    public void adminSoftDelete(Long id) {
        RecordAssessment rec = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de avaliação não encontrado: " + id));

        rec.setActive(false);
        repository.save(rec);
        log.info("RecordAssessment {} marcado como inativo pelo ADMIN", id);
    }

    /**
     * Consulta pública de RecordAssessment por ID (qualquer um com o ID pode
     * acessar)
     */
    @Transactional(readOnly = true)
    public RecordAssessment publicFindById(Long id) {
        log.info("Consulta pública da RecordAssessment com id {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro de avaliação não encontrado: " + id));
    }

    /** Clone de snapshot */
    private QuestionSnapshot cloneSnapshot(QuestionSnapshot src) {
        QuestionSnapshot dst = new QuestionSnapshot();
        dst.setId(src.getId());
        dst.setMultipleChoice(src.getMultipleChoice());
        dst.setNumberLines(src.getNumberLines());
        dst.setEducationLevel(src.getEducationLevel());
        dst.setPersonId(src.getPersonId());
        dst.setHeader(src.getHeader());
        dst.setHeaderImage(src.getHeaderImage());
        dst.setAnswerId(src.getAnswerId());
        dst.setEnable(src.getEnable());
        dst.setAccessLevel(src.getAccessLevel());
        dst.setWeight(src.getWeight());
        dst.setTags(List.copyOf(src.getTags()));

        List<AlternativeSnapshot> altClones = src.getAlternatives().stream()
                .map(a -> {
                    AlternativeSnapshot as = new AlternativeSnapshot();
                    as.setId(a.getId());
                    as.setDescription(a.getDescription());
                    as.setImagePath(a.getImagePath());
                    as.setIsCorrect(a.getIsCorrect());
                    as.setExplanation(a.getExplanation());
                    as.setPosition(a.getPosition());
                    as.setQuestionSnapshot(dst);
                    return as;
                })
                .collect(Collectors.toList());
        dst.setAlternatives(altClones);

        return dst;
    }

    /** Gera gabarito */
    private String computeCorrectKey(List<QuestionSnapshot> snaps) {
        return snaps.stream()
                .map(qs -> {
                    List<AlternativeSnapshot> alts = qs.getAlternatives();
                    for (int j = 0; j < alts.size(); j++) {
                        if (Boolean.TRUE.equals(alts.get(j).getIsCorrect())) {
                            return String.valueOf((char) ('A' + j));
                        }
                    }
                    return "?";
                })
                .collect(Collectors.joining(",", "[", "]"));
    }

    /** Auxiliar → ID usuário atual */
    private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return jwtUtils.extractUserId(auth);
    }

    /** Auxiliar → ADMIN? */
    private boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return jwtUtils.isAdmin(auth);
    }
}

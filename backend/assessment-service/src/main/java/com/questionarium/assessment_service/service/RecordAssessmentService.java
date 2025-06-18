package com.questionarium.assessment_service.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.model.RecordAssessment;
import com.questionarium.assessment_service.repository.RecordAssessmentRepository;
import com.questionarium.assessment_service.security.JwtTokenDecoder;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RecordAssessmentService {

    private final RecordAssessmentRepository repository;
    private final AppliedAssessmentService appliedService;
    private final JwtTokenDecoder jwtUtils;

    public List<RecordAssessment> createFromAppliedAssessment(Long appliedAssessmentId, List<String> studentNames) {
        AppliedAssessment applied = appliedService.findById(appliedAssessmentId);
        int quantity = applied.getQuantity();

        if (studentNames.size() != quantity) {
            throw new BusinessException("Esperado " + quantity + " nomes, mas recebeu " + studentNames.size());
        }

        List<RecordAssessment> created = new ArrayList<>(quantity);

        for (int i = 0; i < quantity; i++) {
            RecordAssessment rec = new RecordAssessment();
            rec.setAppliedAssessment(applied);
            rec.setInstanceIndex(i + 1);
            rec.setStudentName(studentNames.get(i));

            List<QuestionSnapshot> clones = applied.getQuestionSnapshots().stream()
                    .map(orig -> {
                        QuestionSnapshot c = QuestionSnapshot.builder()
                                .question(orig.getQuestion())
                                .multipleChoice(orig.getMultipleChoice())
                                .numberLines(orig.getNumberLines())
                                .educationLevel(orig.getEducationLevel())
                                .header(orig.getHeader())
                                .headerImage(orig.getHeaderImage())
                                .answerId(orig.getAnswerId())
                                .enable(orig.getEnable())
                                .accessLevel(orig.getAccessLevel())
                                .tags(List.copyOf(orig.getTags()))
                                .alternatives(orig.getAlternatives())
                                .weight(orig.getWeight())
                                .build();
                        c.setAppliedAssessment(null);
                        c.setRecordAssessment(rec);
                        return c;
                    })
                    .collect(Collectors.toList());

            if (Boolean.TRUE.equals(applied.getShuffleQuestions())) {
                Collections.shuffle(clones);
            }

            rec.setQuestionOrder(clones.stream()
                    .map(QuestionSnapshot::getQuestion)
                    .collect(Collectors.toList()));

            rec.setQuestionSnapshots(clones);
            rec.setTotalScore(applied.getTotalScore());
            rec.setCorrectAnswerKey(applied.getCorrectAnswerKey());

            created.add(repository.save(rec));
        }

        return created;
    }

    public RecordAssessment create(RecordAssessment entity) {
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public RecordAssessment findById(Long id) {
        RecordAssessment rec = repository.findById(id)
                .filter(RecordAssessment::getActive)
                .orElseThrow(
                        () -> new EntityNotFoundException("Registro de avaliação não encontrado ou inativo: " + id));

        if (isAdmin()) {
            return rec;
        } else if (!rec.getAppliedAssessment().getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("Você não tem permissão para acessar este registro de avaliação");
        }
        return rec;
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findAllActive() {
        if (isAdmin()) {
            return repository.findByActiveTrue();
        } else {
            return repository.findByAppliedAssessmentUserIdAndActiveTrue(getCurrentUserId());
        }
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findByUser(Long userId) {
        if (isAdmin()) {
            return repository.findByAppliedAssessmentUserId(userId);
        } else if (userId.equals(getCurrentUserId())) {
            return repository.findByAppliedAssessmentUserIdAndActiveTrue(userId);
        } else {
            throw new BusinessException("Você não tem permissão para acessar registros de outro usuário");
        }
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findByAppliedAssessment(Long appliedAssessmentId) {
        var applied = appliedService.findById(appliedAssessmentId);
        if (isAdmin() || applied.getUserId().equals(getCurrentUserId())) {
            return repository.findByAppliedAssessmentIdAndActiveTrue(appliedAssessmentId);
        } else {
            throw new BusinessException("Você não tem permissão para acessar registros desta avaliação");
        }
    }

    public RecordAssessment updateObtainedScore(Long id, Double obtainedScore) {
        RecordAssessment rec = findById(id);
        rec.setObtainedScore(obtainedScore);
        return repository.save(rec);
    }

    public RecordAssessment updateStudentAnswerKey(Long id, String studentAnswerKey) {
        RecordAssessment rec = findById(id);
        rec.setStudentAnswerKey(studentAnswerKey);
        return repository.save(rec);
    }

    public void softDelete(Long id) {
        RecordAssessment rec = findById(id);
        if (isAdmin()) {
            throw new BusinessException("Soft-delete de registros deve ser feito com método de admin específico");
        }
        rec.setActive(false);
        repository.save(rec);
    }

    public void adminSoftDelete(Long id) {
        RecordAssessment rec = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de avaliação não encontrado: " + id));
        rec.setActive(false);
        repository.save(rec);
    }

    @Transactional(readOnly = true)
    public RecordAssessment publicFindById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de avaliação não encontrado: " + id));
    }

    private Long getCurrentUserId() {
        return jwtUtils.getCurrentUserId();
    }

    private boolean isAdmin() {
        return jwtUtils.isAdmin();
    }
}

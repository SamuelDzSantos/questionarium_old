package com.questionarium.assessment_service.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.model.RecordAssessment;
import com.questionarium.assessment_service.repository.AppliedAssessmentRepository;
import com.questionarium.assessment_service.repository.RecordAssessmentRepository;
import com.questionarium.assessment_service.security.JwtTokenDecoder;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RecordAssessmentService {

    private final RecordAssessmentRepository repository;
    private final AppliedAssessmentRepository appliedRepo;
    private final JwtTokenDecoder jwtUtils;

    public List<RecordAssessment> createFromAppliedAssessment(Long appliedAssessmentId, List<String> studentNames) {
        AppliedAssessment applied = appliedRepo.findById(appliedAssessmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Avaliação aplicada não encontrada: " + appliedAssessmentId));
        if (!applied.getActive()) {
            throw new BusinessException("Avaliação aplicada está inativa: " + appliedAssessmentId);
        }
        if (!isAdmin() && !applied.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("Você não tem permissão para acessar esta avaliação");
        }

        int quantity = applied.getQuantity();
        if (studentNames.size() != quantity) {
            throw new BusinessException(
                    "Esperado " + quantity + " nomes, mas recebeu " + studentNames.size());
        }

        List<RecordAssessment> created = new ArrayList<>(quantity);
        for (int i = 0; i < quantity; i++) {
            RecordAssessment rec = new RecordAssessment();
            rec.setAppliedAssessment(applied);
            rec.setInstanceIndex(i + 1);
            rec.setStudentName(studentNames.get(i));

            // Clona os snapshots
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
                                .alternatives(new ArrayList<>(orig.getAlternatives()))
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

            rec.setQuestionOrder(
                    clones.stream()
                            .map(QuestionSnapshot::getQuestion)
                            .collect(Collectors.toList()));
            rec.setQuestionSnapshots(clones);
            rec.setTotalScore(applied.getTotalScore());

            // Geração de gabarito em letras
            List<String> letters = IntStream.range(0, clones.size())
                    .mapToObj(idx -> {
                        List<AlternativeSnapshot> alts = clones.get(idx).getAlternatives();
                        int correctIdx = IntStream.range(0, alts.size())
                                .filter(j -> Boolean.TRUE.equals(alts.get(j).getIsCorrect()))
                                .findFirst()
                                .orElseThrow(() -> new BusinessException(
                                        "Alternativa correta não encontrada na questão " +
                                                clones.get(idx).getQuestion()));
                        return String.valueOf((char) ('A' + correctIdx));
                    })
                    .collect(Collectors.toList());
            rec.setCorrectAnswerKeyLetter(letters);

            created.add(repository.save(rec));
        }

        return created;
    }

    @Transactional(readOnly = true)
    public RecordAssessment findById(Long id) {
        RecordAssessment rec = repository.findById(id)
                .filter(RecordAssessment::getActive)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro de avaliação não encontrado ou inativo: " + id));
        if (isAdmin() || rec.getAppliedAssessment().getUserId().equals(getCurrentUserId())) {
            return rec;
        }
        throw new BusinessException("Você não tem permissão para acessar este registro de avaliação");
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findAllActive() {
        if (isAdmin()) {
            return repository.findByActiveTrue();
        }
        return repository.findByAppliedAssessmentUserIdAndActiveTrue(getCurrentUserId());
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findByUser(Long userId) {
        if (isAdmin()) {
            return repository.findByAppliedAssessmentUserId(userId);
        }
        if (userId.equals(getCurrentUserId())) {
            return repository.findByAppliedAssessmentUserIdAndActiveTrue(userId);
        }
        throw new BusinessException("Você não tem permissão para acessar registros de outro usuário");
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findByAppliedAssessment(Long appliedAssessmentId) {
        AppliedAssessment applied = appliedRepo.findById(appliedAssessmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Avaliação aplicada não encontrada: " + appliedAssessmentId));
        if (isAdmin() || applied.getUserId().equals(getCurrentUserId())) {
            return repository.findByAppliedAssessmentIdAndActiveTrue(appliedAssessmentId);
        }
        throw new BusinessException("Você não tem permissão para acessar registros desta avaliação");
    }

    public void softDelete(Long id) {
        RecordAssessment rec = findById(id);
        rec.setActive(false);
        repository.save(rec);
    }

    public void adminSoftDelete(Long id) {
        RecordAssessment rec = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro de avaliação não encontrado: " + id));
        rec.setActive(false);
        repository.save(rec);
    }

    @Transactional(readOnly = true)
    public RecordAssessment publicFindById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro de avaliação não encontrado: " + id));
    }

    private Long getCurrentUserId() {
        return jwtUtils.getCurrentUserId();
    }

    private boolean isAdmin() {
        return jwtUtils.isAdmin();
    }
}

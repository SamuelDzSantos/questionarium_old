package com.questionarium.assessment_service.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.model.RecordAssessment;
import com.questionarium.assessment_service.repository.RecordAssessmentRepository;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;

@Service
@Transactional
public class RecordAssessmentService {

    private final RecordAssessmentRepository repository;
    private final AppliedAssessmentService appliedService;

    public RecordAssessmentService(
            RecordAssessmentRepository repository,
            AppliedAssessmentService appliedService) {
        this.repository = repository;
        this.appliedService = appliedService;
    }

    /**
     * Gera um RecordAssessment para cada nome de estudante, clonando
     * os snapshots da AppliedAssessment e aplicando shuffle e gabarito.
     *
     * @param appliedAssessmentId ID da avaliação aplicada
     * @param studentNames        lista de “apelidos”/nomes dos estudantes (==
     *                            quantidade)
     */
    public List<RecordAssessment> createFromAppliedAssessment(
            Long appliedAssessmentId,
            List<String> studentNames) {

        AppliedAssessment applied = appliedService.findById(appliedAssessmentId);
        List<QuestionSnapshot> baseSnapshots = applied.getQuestionSnapshots();
        int quantity = applied.getQuantity();

        if (studentNames.size() != quantity) {
            throw new IllegalArgumentException(
                    "Esperado " + quantity + " nomes, mas recebeu " + studentNames.size());
        }

        List<RecordAssessment> created = new ArrayList<>(quantity);

        for (int i = 0; i < quantity; i++) {
            // 1) Clone profundo dos snapshots
            List<QuestionSnapshot> snaps = baseSnapshots.stream()
                    .map(this::cloneSnapshot)
                    .collect(Collectors.toList());

            // 2) Embaralha se solicitado
            if (Boolean.TRUE.equals(applied.getShuffleQuestions())) {
                Collections.shuffle(snaps);
            }

            // 3) Monta ordem de IDs
            List<Long> questionOrder = snaps.stream()
                    .map(QuestionSnapshot::getId)
                    .collect(Collectors.toList());

            // 4) Recalcula gabarito "[A,B,C...]"
            String correctKey = computeCorrectKey(snaps);

            // 5) Popula e salva
            RecordAssessment rec = new RecordAssessment();
            rec.setAppliedAssessment(applied);
            rec.setInstanceIndex(i + 1);
            rec.setStudentName(studentNames.get(i));
            rec.setQuestionOrder(questionOrder);
            rec.setQuestionSnapshots(snaps);
            rec.setTotalScore(applied.getTotalScore());
            rec.setCorrectAnswerKey(correctKey);
            // studentAnswerKey, timestamps e active são tratados na entidade
            created.add(repository.save(rec));
        }

        return created;
    }

    /** Clona um QuestionSnapshot e todas as suas AlternativeSnapshot */
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
                    as.setOrder(a.getOrder());
                    return as;
                })
                .collect(Collectors.toList());
        dst.setAlternatives(altClones);

        return dst;
    }

    /** Gera string de gabarito "[A,B,C...]" a partir da lista de snapshots */
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

    /** Cria um único RecordAssessment (caso precise) */
    public RecordAssessment create(RecordAssessment entity) {
        return repository.save(entity);
    }

    /** Lista todas as instâncias ativas */
    @Transactional(readOnly = true)
    public List<RecordAssessment> findAllActive() {
        return repository.findByActiveTrue();
    }

    /** Busca por ID ou lança 404 */
    @Transactional(readOnly = true)
    public RecordAssessment findById(Long id) {
        return repository.findById(id)
                .filter(RecordAssessment::getActive)
                .orElseThrow(() -> new EntityNotFoundException(
                        "RecordAssessment não encontrada ou inativa: " + id));
    }

    /** Lista todas de uma AppliedAssessment específica */
    @Transactional(readOnly = true)
    public List<RecordAssessment> findByAppliedAssessment(Long appliedId) {
        return repository.findByAppliedAssessmentIdAndActiveTrue(appliedId);
    }

    /** Atualiza somente a pontuação obtida */
    public RecordAssessment updateObtainedScore(Long id, Double obtainedScore) {
        RecordAssessment rec = findById(id);
        rec.setObtainedScore(obtainedScore);
        return repository.save(rec);
    }

    /** Soft-delete */
    public void softDelete(Long id) {
        RecordAssessment rec = findById(id);
        rec.setActive(false);
        repository.save(rec);
    }
}

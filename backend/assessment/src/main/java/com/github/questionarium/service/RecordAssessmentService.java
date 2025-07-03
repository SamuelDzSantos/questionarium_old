package com.github.questionarium.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.questionarium.config.exception.BusinessException;
import com.github.questionarium.interfaces.DTOs.AssessmentResultDTO;
import com.github.questionarium.model.AlternativeSnapshot;
import com.github.questionarium.model.AppliedAssessment;
import com.github.questionarium.model.QuestionSnapshot;
import com.github.questionarium.model.RecordAssessment;
import com.github.questionarium.repository.AppliedAssessmentRepository;
import com.github.questionarium.repository.RecordAssessmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RecordAssessmentService {

    private final RecordAssessmentRepository repository;
    private final AppliedAssessmentRepository appliedRepo;

    public List<RecordAssessment> createFromAppliedAssessment(Long appliedAssessmentId, List<String> studentNames,
            Long userId, Boolean isAdmin) {
        AppliedAssessment applied = appliedRepo.findById(appliedAssessmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Avaliação aplicada não encontrada: " + appliedAssessmentId));
        if (!applied.getActive()) {
            throw new BusinessException("Avaliação aplicada está inativa: " + appliedAssessmentId);
        }
        if (!isAdmin && !applied.getUserId().equals(userId)) {
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

            double total = clones.stream()
                    .mapToDouble(QuestionSnapshot::getWeight)
                    .sum();
            rec.setTotalScore(total);

            // Geração de gabarito em letras
            // Z - Questão Discursiva
            List<String> letters = IntStream.range(0, clones.size())
                    .mapToObj(idx -> {
                        if(Boolean.TRUE.equals(clones.get(idx).getMultipleChoice())){
                            List<AlternativeSnapshot> alts = clones.get(idx).getAlternatives();
                            int correctIdx = IntStream.range(0, alts.size())
                                    .filter(j -> Boolean.TRUE.equals(alts.get(j).getIsCorrect()))
                                    .findFirst()
                                    .orElseThrow(() -> new BusinessException(
                                            "Alternativa correta não encontrada na questão " +
                                                    clones.get(idx).getQuestion()));
                            return String.valueOf((char) ('A' + correctIdx));
                        }
                        return "Z"; // Discursiva
                    })
                    .collect(Collectors.toList());
            rec.setCorrectAnswerKeyLetter(letters);

            // Inicializa lista de respostas do aluno vazia
            rec.setStudentAnswerKey(new ArrayList<>());

            created.add(repository.save(rec));
        }

        return created;
    }

    @Transactional(readOnly = true)
    public RecordAssessment findById(Long id, Long userId, Boolean isAdmin) {
        RecordAssessment rec = repository.findById(id)
                .filter(RecordAssessment::getActive)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro de avaliação não encontrado ou inativo: " + id));
        if (isAdmin || rec.getAppliedAssessment().getUserId().equals(userId)) {
            return rec;
        }
        throw new BusinessException("Você não tem permissão para acessar este registro de avaliação");
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findAllActive(Long userId, Boolean isAdmin) {
        if (isAdmin) {
            return repository.findByActiveTrue();
        }
        return repository.findByAppliedAssessmentUserIdAndActiveTrue(userId);
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findByUser(Long userId, Boolean isAdmin) {
        if (isAdmin) {
            return repository.findByAppliedAssessmentUserId(userId);
        }
        if (userId.equals(userId)) {
            return repository.findByAppliedAssessmentUserIdAndActiveTrue(userId);
        }
        throw new BusinessException("Você não tem permissão para acessar registros de outro usuário");
    }

    @Transactional(readOnly = true)
    public List<RecordAssessment> findByAppliedAssessment(Long appliedAssessmentId, Long userId, Boolean isAdmin) {
        AppliedAssessment applied = appliedRepo.findById(appliedAssessmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Avaliação aplicada não encontrada: " + appliedAssessmentId));
        if (isAdmin || applied.getUserId().equals(userId)) {
            return repository.findByAppliedAssessmentIdAndActiveTrue(appliedAssessmentId);
        }
        throw new BusinessException("Você não tem permissão para acessar registros desta avaliação");
    }

    public void softDelete(Long id, Long userId, Boolean isAdmin) {
        RecordAssessment rec = findById(id, userId, isAdmin);
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

    /**
     * Calcula e persiste a nota obtida pelo aluno para um RecordAssessment.
     *
     * @param id        ID do RecordAssessment
     * @param answerKey Lista de letras com as respostas do aluno
     * @return Nota obtida (soma dos pesos das questões corretas)
     */
    public double calculateScore(Long id, List<String> answerKey, Long userId, Boolean isAdmin) {
        RecordAssessment rec = repository.findById(id)
                .filter(RecordAssessment::getActive)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registro de avaliação não encontrado ou inativo: " + id));
        if (!isAdmin && !rec.getAppliedAssessment().getUserId().equals(userId)) {
            throw new BusinessException("Você não tem permissão para corrigir este registro");
        }
        /// A B C D 2 3 4 5
        // Armazena diretamente a lista de respostas do aluno
        rec.setStudentAnswerKey(new ArrayList<>(answerKey));
        // Alternativas corretas A B C D
        // Calcula a pontuação obtida
        List<String> correct = rec.getCorrectAnswerKeyLetter();
        List<QuestionSnapshot> snaps = rec.getQuestionSnapshots();
        // ++ 1
        // X - Em branco, invalido
        // Y - Discursiva correta
        // N - Discursiva incorreta
        // P - Discursiva não corrigida  
        double score = IntStream.range(0, correct.size())
                .filter(i -> {
                    if(Boolean.TRUE.equals(snaps.get(i).getMultipleChoice())){
                        return correct.get(i).equals(answerKey.get(i));
                    }
                    if (answerKey.get(i).equals("Y")) {
                        return true;
                    }
                    if (answerKey.get(i).equals("N") || answerKey.get(i).equals("P")) {
                        return false;
                    }
                    return false;
                })
                .mapToDouble(i -> snaps.get(i).getWeight())
                .sum();
        rec.setObtainedScore(score);

        repository.save(rec);
        return score;
    }

    @Transactional(readOnly = true)
    public AssessmentResultDTO getAssessmentResult(Long id) {
        
        RecordAssessment record = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de avaliação não encontrado: " + id));

        AppliedAssessment applied = record.getAppliedAssessment();

        return new AssessmentResultDTO(
                applied.getInstitution(),
                applied.getDepartment(),
                applied.getCourse(),
                applied.getClassroom(),
                applied.getProfessor(),
                record.getStudentName(),
                record.getTotalScore(),
                record.getObtainedScore(),
                record.getCorrectAnswerKeyLetter(),
                record.getStudentAnswerKey()
        );
    }

}

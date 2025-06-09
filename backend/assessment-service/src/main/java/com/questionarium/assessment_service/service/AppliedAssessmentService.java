package com.questionarium.assessment_service.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.client.QuestionClient;
import com.questionarium.assessment_service.dto.AnswerKeyDTO;
import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.repository.AppliedAssessmentRepository;
import com.questionarium.assessment_service.repository.AssessmentModelRepository;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class AppliedAssessmentService {

        private final AppliedAssessmentRepository appliedRepo;
        private final AssessmentModelRepository modelRepo;
        private final QuestionClient questionClient;

        public AppliedAssessmentService(
                        AppliedAssessmentRepository appliedRepo,
                        AssessmentModelRepository modelRepo,
                        QuestionClient questionClient) {
                this.appliedRepo = appliedRepo;
                this.modelRepo = modelRepo;
                this.questionClient = questionClient;
        }

        /** Lista todas as AppliedAssessment ativas */
        @Transactional(readOnly = true)
        public List<AppliedAssessment> findAllActive() {
                log.info("Buscando todas as avaliações aplicadas ativas");
                return appliedRepo.findByActiveTrue();
        }

        /** Busca uma AppliedAssessment ativa por id ou lança 404 */
        @Transactional(readOnly = true)
        public AppliedAssessment findById(Long id) {
                log.info("Buscando AppliedAssessment com id {}", id);
                return appliedRepo.findById(id)
                                .filter(AppliedAssessment::getActive)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Avaliação aplicada não encontrada ou inativa: " + id));
        }

        /** Lista todas as AppliedAssessment ativas de um usuário */
        @Transactional(readOnly = true)
        public List<AppliedAssessment> findByUser(Long userId) {
                log.info("Buscando avaliações aplicadas ativas do usuário {}", userId);
                return appliedRepo.findByUserIdAndActiveTrue(userId);
        }

        /**
         * Aplica um template de AssessmentModel, congelando todos os dados das questões
         * e gerando o gabarito.
         */
        public AppliedAssessment applyAssessment(
                        Long modelId,
                        LocalDate applicationDate,
                        Integer quantity,
                        Boolean shuffleQuestions) {

                log.info("Iniciando aplicação da avaliação do modelo {}, quantidade {}, data {}, embaralhar {}",
                                modelId, quantity, applicationDate, shuffleQuestions);

                // 1) Carrega o template
                AssessmentModel model = modelRepo.findById(modelId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Modelo de avaliação não encontrado: " + modelId));

                // 2) Monta QuestionSnapshots
                List<QuestionSnapshot> snapshots = model.getQuestions().stream()
                                .map(qw -> {
                                        var qDto = questionClient.getQuestion(qw.getQuestionId());
                                        var alts = questionClient.getAlternatives(qw.getQuestionId());

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

                // 3) Embaralha se solicitado
                if (Boolean.TRUE.equals(shuffleQuestions)) {
                        Collections.shuffle(snapshots);
                }

                // 4) Monta gabarito
                List<Long> ids = snapshots.stream()
                                .map(QuestionSnapshot::getId)
                                .collect(Collectors.toList());
                List<AnswerKeyDTO> keys = questionClient.getAnswerKeys(ids);
                String correctKey = keys.stream()
                                .map(AnswerKeyDTO::getAnswerKey)
                                .collect(Collectors.joining(",", "[", "]"));

                // 5) Monta entidade
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

                // 6) Persiste
                AppliedAssessment saved = appliedRepo.save(applied);
                log.info("Avaliação aplicada {} salva com sucesso", saved.getId());
                return saved;
        }

        /** Soft-delete de uma avaliação aplicada: marca como inativa */
        public void softDelete(Long id) {
                log.info("Iniciando soft-delete da AppliedAssessment com id {}", id);
                AppliedAssessment applied = appliedRepo.findById(id)
                                .filter(AppliedAssessment::getActive)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Avaliação aplicada não encontrada ou já inativa: " + id));

                applied.setActive(false);
                appliedRepo.save(applied);
                log.info("Avaliação aplicada {} marcada como inativa", id);
        }
}

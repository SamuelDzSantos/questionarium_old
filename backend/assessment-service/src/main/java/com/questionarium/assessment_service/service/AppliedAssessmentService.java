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

@Service
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
                return appliedRepo.findByActiveTrue();
        }

        /** Busca uma AppliedAssessment ativa por id ou lança 404 */
        @Transactional(readOnly = true)
        public AppliedAssessment findById(Long id) {
                return appliedRepo.findById(id)
                                .filter(AppliedAssessment::getActive)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "AppliedAssessment não encontrada ou inativa: " + id));
        }

        /** Lista todas as AppliedAssessment ativas de um usuário */
        @Transactional(readOnly = true)
        public List<AppliedAssessment> findByUser(Long userId) {
                return appliedRepo.findByUserIdAndActiveTrue(userId);
        }

        /**
         * Aplica um template de AssessmentModel, “congelando” todos os dados das
         * questões
         * e gerando o gabarito.
         *
         * @param modelId          ID do template (AssessmentModel)
         * @param applicationDate  Data de aplicação (null → hoje)
         * @param quantity         Quantas vezes aplicar
         * @param shuffleQuestions Se deve embaralhar a ordem
         */
        public AppliedAssessment applyAssessment(
                        Long modelId,
                        LocalDate applicationDate,
                        Integer quantity,
                        Boolean shuffleQuestions) {

                // 1) Carrega o template
                AssessmentModel model = modelRepo.findById(modelId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "AssessmentModel não encontrado: " + modelId));

                // 2) Para cada QuestionWeight do template, monta um QuestionSnapshot completo
                List<QuestionSnapshot> snapshots = model.getQuestions().stream()
                                .map(qw -> {
                                        // busca dados da questão e das alternativas
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

                                        // popula alternativas
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

                // 4) Busca os gabaritos (AnswerKeyDTO) e monta a string "[A,B,C,…]"
                List<Long> ids = snapshots.stream()
                                .map(QuestionSnapshot::getId)
                                .collect(Collectors.toList());
                List<AnswerKeyDTO> keys = questionClient.getAnswerKeys(ids);
                String correctKey = keys.stream()
                                .map(AnswerKeyDTO::getAnswerKey)
                                .collect(Collectors.joining(",", "[", "]"));

                // 5) Monta a entidade AppliedAssessment com todos os dados
                AppliedAssessment applied = new AppliedAssessment();
                applied.setDescription(model.getDescription());
                applied.setQuestionSnapshots(snapshots);
                applied.setTotalScore(
                                snapshots.stream().mapToDouble(QuestionSnapshot::getWeight).sum());
                // datas de criação/atualização serão ajustadas pelos @PrePersist/@PreUpdate
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

                // 6) Persiste e retorna
                return appliedRepo.save(applied);
        }

        /**
         * Soft-delete de uma avaliação aplicada: marca como inactive.
         *
         * @param id ID da AppliedAssessment
         * @throws EntityNotFoundException se não existir ou já estiver inativa
         */
        public void softDelete(Long id) {
                AppliedAssessment applied = appliedRepo.findById(id)
                                .filter(AppliedAssessment::getActive)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "AppliedAssessment não encontrada ou já inativa: " + id));

                applied.setActive(false);
                // o @PreUpdate vai ajustar updateDateTime
                appliedRepo.save(applied);
        }

}

package br.com.questionarium.assessment_service.services;

import java.util.List;
import java.util.stream.Collectors;

import br.com.questionarium.assessment_service.dto.StudentAssessmentDTO;
import br.com.questionarium.assessment_service.models.AppliedQuestion;
import br.com.questionarium.assessment_service.repositories.AppliedQuestionRepository;
import br.com.questionarium.assessment_service.repositories.StudentAssessmentRepository;

public class StudentAssessmentService {
    private final AppliedQuestionRepository appliedQuestionRepository;
    private final StudentAssessmentRepository studentAssessmentRepository;

    public StudentAssessmentService(AppliedQuestionRepository appliedQuestionRepository,
            StudentAssessmentRepository studentAssessmentRepository) {
        this.appliedQuestionRepository = appliedQuestionRepository;
        this.studentAssessmentRepository = studentAssessmentRepository;
    }

    public StudentAssessmentDTO createStudentAssessment(Long appliedAssessmentId, Long appliedQuestionId) {
        // Busca as questões relacionadas
        List<AppliedQuestion> questions = appliedQuestionRepository.findByAppliedAssessmentId(appliedAssessmentId);

        // Monta o campo ANSWERKEY (ordem + resposta)
        List<String> answerKey = questions.stream()
                .sorted((q1, q2) -> q1.getOrder().compareTo(q2.getOrder())) // Ordena pelas ordens
                .map(q -> q.getOrder() + "," + q.getAnswer()) // Combina ordem e resposta
                .collect(Collectors.toList());

        // Cria o DTO
        return new StudentAssessmentDTO(null, appliedAssessmentId, appliedQuestionId, answerKey);
    }
}

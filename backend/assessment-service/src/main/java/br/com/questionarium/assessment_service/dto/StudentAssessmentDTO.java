package br.com.questionarium.assessment_service.dto;

import java.util.List;

import lombok.Data;

@Data
public class StudentAssessmentDTO {
    
    private Long id;
    private Long appliedAssessmentId;
    private Long appliedQuestionId;
    private List<String> answerKey;

    public StudentAssessmentDTO(Long id, Long appliedAssessmentId, Long appliedQuestionId, List<String> answerKey) {
        this.id = id;
        this.appliedAssessmentId = appliedAssessmentId;
        this.appliedQuestionId = appliedQuestionId;
        this.answerKey = answerKey;
    }
}

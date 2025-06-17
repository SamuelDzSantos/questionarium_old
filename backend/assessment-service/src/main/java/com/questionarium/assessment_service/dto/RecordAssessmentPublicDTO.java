package com.questionarium.assessment_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordAssessmentPublicDTO {
    private String studentName;
    private List<Long> questionOrder;
    private List<QuestionDTO> questionSnapshots;
    private String correctAnswerKey;
    private String studentAnswerKey;
    private Double obtainedScore;
}

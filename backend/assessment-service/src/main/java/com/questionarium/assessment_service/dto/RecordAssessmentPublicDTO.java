package com.questionarium.assessment_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordAssessmentPublicDTO {
    private String studentName;
    private List<Long> questionOrder;
    private List<QuestionSnapshotDTO> questionSnapshots;
    private String correctAnswerKey;
    private String studentAnswerKey;
    private Double obtainedScore;
}

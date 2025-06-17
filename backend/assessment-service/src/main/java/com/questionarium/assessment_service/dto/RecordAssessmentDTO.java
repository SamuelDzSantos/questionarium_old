package com.questionarium.assessment_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordAssessmentDTO {
    private Long id;
    private Long appliedAssessmentId;
    private Integer instanceIndex;
    private String studentName;
    private List<Long> questionOrder;
    private List<QuestionDTO> questionSnapshots;
    private Double totalScore;
    private Double obtainedScore;
    private String correctAnswerKey;
    private String studentAnswerKey;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;
}

package br.com.questionarium.assessment_service.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AppliedAssessmentRequest {
    private Long assessmentId;
    private int quantity;
    private LocalDate applicationDate;
}

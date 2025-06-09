package com.questionarium.assessment_service.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentModelDTO {
    private Long id;
    private String description;

    @NotNull
    private Long userId;

    // HEADER
    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;
    private String instructions;
    private String image;

    @NotNull
    private List<QuestionWeightDTO> questions;
}

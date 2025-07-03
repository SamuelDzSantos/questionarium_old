package com.github.questionarium.interfaces.DTOs;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyAssessmentRequestDTO {
    @NotNull
    private Long modelId;

    @NotNull
    private Integer quantity;

    @NotNull
    private LocalDate applicationDate;

    @NotNull
    private Boolean shuffleQuestions;

    private String description;
}

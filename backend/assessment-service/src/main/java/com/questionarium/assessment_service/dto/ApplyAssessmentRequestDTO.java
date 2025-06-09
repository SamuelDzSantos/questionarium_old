package com.questionarium.assessment_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyAssessmentRequestDTO {
    @NotNull
    private Long modelId;

    /** Quantas vezes vai aplicar essa avaliação agora */
    @NotNull
    private Integer quantity;

    /** Data em que está aplicando (pode ser hoje se null) */
    private LocalDate applicationDate;

    /** Se deve embaralhar as questões */
    @NotNull
    private Boolean shuffleQuestions;
}

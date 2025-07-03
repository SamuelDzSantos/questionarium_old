package com.github.questionarium.interfaces.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWeightDTO {
    @NotNull
    private Long questionId;

    @NotNull
    private Double weight;
    private Integer questionOrder;

}

package com.github.questionarium.types;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWeightDTO {
    @NotNull
    private Long questionId;

    @NotNull
    private Double weight;
}

package com.github.questionarium.model;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWeight {
    @Column(name = "question_id")
    @NotNull
    private Long questionId;

    @Column(name = "weight")
    @NotNull
    private Double weight;

    @NonNull
    private Integer questionOrder;
}

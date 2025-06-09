package com.questionarium.assessment_service.snapshot;

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
public class AlternativeSnapshot {
    @Column(name = "alternative_id")
    private Long id;

    @Column(name = "description", columnDefinition = "TEXT")
    @NotNull
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_correct")
    @NotNull
    private Boolean isCorrect;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "alternative_order")
    @NotNull
    private Integer order;
}

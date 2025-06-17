package com.questionarium.assessment_service.snapshot;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlternativeSnapshot {

    @Column(name = "alternative", nullable = false)
    @NotNull
    private Long alternative;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_correct", nullable = false)
    @NotNull
    private Boolean isCorrect;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "position", nullable = false)
    @NotNull
    private Integer position;
}
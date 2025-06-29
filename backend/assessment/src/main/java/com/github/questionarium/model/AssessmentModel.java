package com.github.questionarium.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "assessment_model")
@Data
public class AssessmentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "assessment_model_questions", joinColumns = @JoinColumn(name = "assessment_model_id"))
    @OrderColumn(name = "position")
    private List<QuestionWeight> questions = new ArrayList<>();

    @Column(updatable = false)
    @NotNull
    private LocalDateTime creationDateTime;

    @NotNull
    private LocalDateTime updateDateTime;

    @NotNull
    private Long userId;

    // INFO DO HEADER
    private String institution;

    private String department;

    private String course;

    private String classroom;

    private String professor;

    private String instructions;

    private String image;

    @PrePersist
    protected void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (creationDateTime == null) {
            creationDateTime = now;
            updateDateTime = now;
        }
    }

    @PreUpdate
    protected void onPreUpdate() {
        updateDateTime = LocalDateTime.now();
    }
}
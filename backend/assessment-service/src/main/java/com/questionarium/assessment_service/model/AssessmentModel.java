package com.questionarium.assessment_service.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @CreatedDate
    @Column(updatable = false)
    @NotNull
    private LocalDateTime creationDateTime;

    @LastModifiedDate
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

}
package com.questionarium.assessment_service.snapshot;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSnapshot {

    @Column(name = "question_id")
    private Long id;

    @Column(name = "multiple_choice")
    @NotNull
    private Boolean multipleChoice;

    @Column(name = "number_lines")
    @NotNull
    private Integer numberLines;

    @Column(name = "education_level")
    @NotNull
    private String educationLevel;

    @Column(name = "person_id")
    @NotNull
    private Long personId;

    @Column(name = "header", columnDefinition = "TEXT")
    @NotNull
    private String header;

    @Column(name = "header_image")
    private String headerImage;

    @Column(name = "answer_id")
    @NotNull
    private Long answerId;

    @Column(name = "enable")
    @NotNull
    private Boolean enable;

    @Column(name = "access_level")
    @NotNull
    private String accessLevel;

    @ElementCollection
    @CollectionTable(name = "student_assessment_question_snapshot_tags", joinColumns = @JoinColumn(name = "student_assessment_id"))
    @Column(name = "tag")
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "student_assessment_question_snapshot_alternatives", joinColumns = @JoinColumn(name = "student_assessment_id"))
    @OrderColumn(name = "position")
    private List<AlternativeSnapshot> alternatives;

    @Column(name = "weight")
    @NotNull
    private Double weight;
}

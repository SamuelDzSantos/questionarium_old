package com.questionarium.assessment_service.snapshot;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.model.RecordAssessment;

@Entity
@Table(name = "question_snapshots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relacionamento com a AppliedAssessment
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "applied_assessment_id")
    private AppliedAssessment appliedAssessment;

    // **novo**: relacionamento com RecordAssessment
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "record_assessment_id")
    private RecordAssessment recordAssessment;

    @Column(name = "question", nullable = false)
    private Long question;

    @Column(name = "multiple_choice", nullable = false)
    @NotNull
    private Boolean multipleChoice;

    @Column(name = "number_lines", nullable = false)
    @NotNull
    private Integer numberLines;

    @Column(name = "education_level", nullable = false)
    @NotNull
    private String educationLevel;

    @Column(name = "header", columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String header;

    @Column(name = "header_image")
    private String headerImage;

    @Column(name = "answer_id", nullable = false)
    @NotNull
    private Long answerId;

    @Column(name = "enable", nullable = false)
    @NotNull
    private Boolean enable;

    @Column(name = "access_level", nullable = false)
    @NotNull
    private String accessLevel;

    @ElementCollection
    @CollectionTable(name = "snapshot_tags", joinColumns = @JoinColumn(name = "parent_snapshot_id"))
    @Column(name = "tag")
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "snapshot_alternatives", joinColumns = @JoinColumn(name = "parent_snapshot_id"))
    @Builder.Default
    private List<AlternativeSnapshot> alternatives = new ArrayList<>();

    @Column(name = "weight", nullable = false)
    @NotNull
    private Double weight;
}
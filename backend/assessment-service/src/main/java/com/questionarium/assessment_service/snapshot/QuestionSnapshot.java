package com.questionarium.assessment_service.snapshot;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question_snapshots")
@EqualsAndHashCode(exclude = {"alternatives", "recordAssessment"})
@ToString(exclude = {"alternatives"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_question_id")
    @NotNull
    private Long originalQuestionId; // ID da questão original

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

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "question_snapshot_tags", joinColumns = @JoinColumn(name = "question_snapshot_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "questionSnapshot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AlternativeSnapshot> alternatives = new ArrayList<>();

    @Column(name = "weight")
    @NotNull
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_assessment_id")
    private com.questionarium.assessment_service.model.RecordAssessment recordAssessment; // FK para RecordAssessment
}

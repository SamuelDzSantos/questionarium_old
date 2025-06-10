package com.questionarium.assessment_service.snapshot;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "alternative_snapshots")
@EqualsAndHashCode(exclude = { "questionSnapshot" })
@ToString(exclude = { "questionSnapshot" })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlternativeSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID próprio do snapshot

    @Column(name = "original_alternative_id")
    private Long originalAlternativeId; // ID da alternativa original

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

    @Column(name = "alternative_position")
    @NotNull
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_snapshot_id", nullable = false)
    private QuestionSnapshot questionSnapshot;

}

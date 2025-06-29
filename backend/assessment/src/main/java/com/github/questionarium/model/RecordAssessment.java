package com.github.questionarium.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "record_assessment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RecordAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Qual AppliedAssessment gerou este registro */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "applied_assessment_id", nullable = false)
    private AppliedAssessment appliedAssessment;

    /** Índice dentro do lote (1..N) */
    @NotNull
    private Integer instanceIndex;

    /** “Apelido” ou nome que identifica o estudante */
    @Column(name = "student_name", nullable = true)
    private String studentName;

    /** Ordem final das questões (pode ter sido embaralhada) */
    @ElementCollection
    @CollectionTable(name = "record_assessment_question_order", joinColumns = @JoinColumn(name = "record_assessment_id"))
    @OrderColumn(name = "position")
    @Column(name = "question_id", nullable = false)
    private List<Long> questionOrder;

    /** “Foto” completa de cada questão */

    @OneToMany(mappedBy = "recordAssessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)

    @OrderColumn(name = "position")
    private List<QuestionSnapshot> questionSnapshots = new ArrayList<>();

    /** Soma dos weights das questões */
    @NotNull
    @Column(name = "total_score", nullable = false)
    private Double totalScore;

    /** Pontuação que o aluno obteve */
    @Column(name = "obtained_score")
    private Double obtainedScore;
    /** NOVO: gabarito em letras (A, B, C…) para cada questão */
    @ElementCollection
    @CollectionTable(name = "record_letter_keys", joinColumns = @JoinColumn(name = "record_assessment_id"))
    @Column(name = "letter")
    private List<String> correctAnswerKeyLetter = new ArrayList<>();

    /** Gabarito fornecido pelo aluno */
    @ElementCollection
    @CollectionTable(name = "record_student_keys", joinColumns = @JoinColumn(name = "record_assessment_id"))
    @OrderColumn(name = "position")
    @Column(name = "letter", nullable = false)
    private List<String> studentAnswerKey = new ArrayList<>();

    /** Data/hora de criação (preenchido automaticamente) */
    @NotNull
    @Column(name = "creation_date_time", updatable = false, nullable = false)
    private LocalDateTime creationDateTime;

    /** Data/hora da última atualização (preenchido automaticamente) */
    @NotNull
    @Column(name = "update_date_time", nullable = false)
    private LocalDateTime updateDateTime;

    /** Soft-delete */
    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @PrePersist
    protected void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (creationDateTime == null) {
            creationDateTime = now;
            updateDateTime = now;
        }
        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onPreUpdate() {
        updateDateTime = LocalDateTime.now();
    }
}

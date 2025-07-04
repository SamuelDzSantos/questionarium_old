package com.github.questionarium.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "applied_assessment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class AppliedAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String description;

    // QUESTÕES SNAPSHOT

    @OneToMany(mappedBy = "appliedAssessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)

    @OrderColumn(name = "position")
    @ToString.Exclude
    private List<QuestionSnapshot> questionSnapshots = new ArrayList<>();

    // PONTUAÇÃO TOTAL DA APLICAÇÃO
    @NotNull
    @Column(name = "total_score")
    private Double totalScore;

    // DATA/HORA DA CRIAÇÃO
    @Column(updatable = false)
    @NotNull
    private LocalDateTime creationDateTime;

    // DATA/HORA DA ÚLTIMA ATUALIZAÇÃO
    @NotNull
    private LocalDateTime updateDateTime;

    @NotNull
    private Long userId;

    // DADOS DO CABEÇALHO DA APLICAÇÃO
    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;
    private String instructions;
    private String image;

    // DATA DE APLICAÇÃO
    @NotNull
    private LocalDate applicationDate;

    // QUANTIDADE DE AVALIACOES APLICADAS
    @NotNull
    private Integer quantity;

    // EMBARALHAMENTO DE QUESTÕES
    @NotNull
    private Boolean shuffleQuestions;

    // SOFTDELETE
    @NotNull
    @Column(name = "active")
    private Boolean active = true;

    // GABARITO DE RESPOSTA CORRETA
    @NotNull
    @Column(name = "correct_answer_key", columnDefinition = "TEXT")
    private String correctAnswerKey;

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

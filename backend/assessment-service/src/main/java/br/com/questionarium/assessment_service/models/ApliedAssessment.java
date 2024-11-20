package br.com.questionarium.assessment_service.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "applied_assessment")
@Data
public class ApliedAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<Long> questions;

    @Column
    private String answerKey; // GABARITO DAS QUESTOES

    private LocalDate creationDate;
    private LocalDate applicationDate;
    private int quantity;

    @Column(nullable = false)
    private Long userId; // FK DO USER

    private Long headerId; // FK DO HEADER

    private boolean status;

    @PrePersist
    public void prePersist() {
        // DEFINE DATA CRIACAO
        if (this.creationDate == null) {
            this.creationDate = LocalDate.now();
        }
    }

}

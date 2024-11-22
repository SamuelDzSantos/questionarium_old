package br.com.questionarium.assessment_service.models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ElementCollection
    @CollectionTable(name = "applied_answer_key", joinColumns = @JoinColumn(name = "applied_assessment_id"))
    @MapKeyColumn(name = "question_id") // Define o nome da coluna para a chave do mapa
    @Column(name = "answer") // Define o nome da coluna para o valor do mapa
    private Map<Long, String> answerKey = new HashMap<>();

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

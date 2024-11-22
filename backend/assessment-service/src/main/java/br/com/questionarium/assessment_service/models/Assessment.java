package br.com.questionarium.assessment_service.models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "assessment")
@Data
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<Long> questions; // [10, 11, 12, 13] -> Q = [12, 13, 10 ,11, ]

    @ElementCollection
    @CollectionTable(name = "answer_key", joinColumns = @JoinColumn(name = "assessment_id"))
    @MapKeyColumn(name = "question_id") // Define o nome da coluna para a chave do mapa
    @Column(name = "answer") // Define o nome da coluna para o valor do mapa
    private Map<Long, String> answerKey = new HashMap<>();

    private LocalDate creationDate;

    @Column(nullable = false)
    private Long userId; // FK DO USER

    private Long headerId; // FK DO HEADER

    @PrePersist
    public void prePersist() {
        // DEFINE DATA CRIACAO
        if (this.creationDate == null) {
            this.creationDate = LocalDate.now();
        }
    }
}

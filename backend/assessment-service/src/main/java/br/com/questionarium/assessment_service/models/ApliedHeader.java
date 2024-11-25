package br.com.questionarium.assessment_service.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "applied_header")
@Data
public class ApliedHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institution;

    private String department;

    private String course;

    private String classroom;

    private String professor; // Nome do professor no cabeçalho

    @Lob
    private String instructions; // Orientações podem ser longas, mantendo como LOB

    @Lob
    private byte[] image; // Imagem do cabeçalho

    private LocalDate creationDate;

    private Long originalHeaderId; // Referência ao cabeçalho original

    @Column(nullable = false)
    private Long userId; // ID do usuário que aplicou

    @PrePersist
    public void prePersist() {
        // Define a data de criação caso não esteja definida
        if (this.creationDate == null) {
            this.creationDate = LocalDate.now();
        }
    }
}

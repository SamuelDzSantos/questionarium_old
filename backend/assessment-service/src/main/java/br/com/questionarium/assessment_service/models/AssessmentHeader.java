package br.com.questionarium.assessment_service.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "assessment_header")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institution;

    private String department;

    private String course;

    private String classroom;

    private String professor; //NOME QUE APARECE NO CABECALHO

    // @Lob
    private String instructions;

    private String image;

    private LocalDate creationDate;

    @Column(nullable = false)
    private Long userId; // FK do usuário que criou

    @PrePersist
    public void prePersist() {
        // DEFINE DATA CRIACAO
        if (this.creationDate == null) {
            this.creationDate = LocalDate.now();
        }
    }
}

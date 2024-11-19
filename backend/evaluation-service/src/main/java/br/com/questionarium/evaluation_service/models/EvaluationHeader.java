package br.com.questionarium.evaluation_service.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "evaluation_header")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String instituicao;

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private String curso;

    @Column(nullable = false)
    private String turma;

    @Column(nullable = false)
    private String professor;

    @Lob
    private String instruction;

    @Lob
    private byte[] image;

    @Column(nullable = false)
    private LocalDate creationDate;
}

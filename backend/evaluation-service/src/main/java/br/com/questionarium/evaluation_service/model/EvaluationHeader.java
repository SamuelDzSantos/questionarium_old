package br.com.questionarium.evaluation_service.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

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

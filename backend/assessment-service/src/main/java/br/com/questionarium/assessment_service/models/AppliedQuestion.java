package br.com.questionarium.assessment_service.models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "applied_question")
@Data
public class AppliedQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applied_assessment_id", nullable = false)
    private AppliedAssessment appliedAssessment;

    @Column(name = "id_question", nullable = false)
    private Long idQuestion;

    @Column(name = "question_order", nullable = false)
    private Integer order;

    @Column(name = "multiple_choice")
    private boolean multipleChoice;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany()
    @Column(name = "alternative")
    private Set<Alternative> alternatives;

    @Column(name = "answer")
    private String answer;

    @Column(name = "weight")
    private Double weight;

}
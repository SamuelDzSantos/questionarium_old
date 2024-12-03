package br.com.questionarium.assessment_service.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_assessment")
@Data
public class StudentAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "id_appliedassessment", nullable = false)
    private AppliedAssessment appliedAssessmentId;

    @Column(name = "id_appliedquestion", nullable = false)
    private Long appliedQuestionId;

    @Column(name = "answer_key")
    private List<String> answerKey; //tratado no service

    @Column(name = "answer_key_student")
    private List<String> answerKeyStudent; //cria nulo

    @Column(name = "weight")
    private List<Double> weight;

    @Column(name = "score")
    private Double score;
}

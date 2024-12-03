package br.com.questionarium.assessment_service.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "applied_assessment")
@Data
public class AppliedAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long originalAssessmentId;

    @Column(nullable = false)
    private Long userId;

    private String institution;

    private String department;

    private String course;

    private String classroom;

    private String professor;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    private String image;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private LocalDate applicationDate;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private boolean shuffle;

    @OneToMany(mappedBy = "appliedAssessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppliedQuestion> appliedQuestions;

    @PrePersist
    public void prePersist() {
        // DEFINE DATA CRIACAO
        if (this.creationDate == null) {
            this.creationDate = LocalDate.now();
        }
    }
}

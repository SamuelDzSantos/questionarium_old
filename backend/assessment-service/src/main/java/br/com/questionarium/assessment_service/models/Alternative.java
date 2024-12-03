package br.com.questionarium.assessment_service.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "alternative")
@Data
@IdClass(AlternativeId.class)
public class Alternative {

    @Id
    private Long appliedAssessmentId;

    @Id
    private Long questionId;

    @Column(nullable = false)
    private char option;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column
    private String explanation;



}

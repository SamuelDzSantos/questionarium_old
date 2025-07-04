package com.github.questionarium.interfaces.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppliedAssessmentDTO {

    private Long id;
    private String description;
    private Double totalScore;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;
    private Long userId;

    // campos de header (cópia do modelo)
    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;
    private String instructions;
    private String image;

    private LocalDate applicationDate;
    private Integer quantity;
    private Boolean shuffleQuestions;
    private Boolean active;
    private String correctAnswerKey;

    /** A “foto” completa das questões */
    private List<QuestionDTO> questionSnapshots;
}

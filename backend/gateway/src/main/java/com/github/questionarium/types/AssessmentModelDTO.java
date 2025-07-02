package com.github.questionarium.types;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentModelDTO {
    private Long id;
    private String description;
    private Long userId;

    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;
    private String instructions;
    private String image;

    private List<QuestionWeightDTO> questions;

    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;
}

package com.github.questionarium.interfaces.DTOs;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssessmentModelRequestDTO {

    private String description;
    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;
    private String instructions;
    private String image;

    @NotNull
    private List<QuestionDTO> questions;
}

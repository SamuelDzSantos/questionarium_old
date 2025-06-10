package com.questionarium.assessment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlternativeDTO {
    private Long id;
    private String description;
    private String imagePath;
    private Boolean isCorrect;
    private String explanation;
    private Integer alternativePosition;
}

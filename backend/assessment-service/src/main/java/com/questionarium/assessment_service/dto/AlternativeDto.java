package com.questionarium.assessment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlternativeDTO {

    private Long id;
    private String description;
    private String imagePath;
    private Boolean isCorrect;
    private String explanation;
    private Integer alternativeOrder;
}
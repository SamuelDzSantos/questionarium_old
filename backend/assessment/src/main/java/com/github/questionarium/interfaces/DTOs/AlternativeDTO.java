package com.github.questionarium.interfaces.DTOs;

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
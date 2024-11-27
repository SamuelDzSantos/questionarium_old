package br.com.questionarium.question_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AlternativeDTO {
    
    private Long id;
    private String description;
    private String imagePath;
    private Boolean isCorrect;
    private String explanation;
    private Long question_id;
}

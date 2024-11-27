package br.com.questionarium.question_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HeaderDTO {
    
    private Long id;
    private String content;
    private String imagePath;
}
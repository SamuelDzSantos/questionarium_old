package br.com.questionarium.evaluation_service.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationHeaderUserDTO {
    private Long id;
    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;
    private LocalDate creationDate;
    private Long userId;
}

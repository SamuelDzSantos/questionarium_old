package com.questionarium.assessment_service.dto;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {

    private Long question; // id da questão
    private Boolean multipleChoice;
    private Integer numberLines;
    private String educationLevel;
    private String header;
    private String headerImage;
    private Long answerId;
    private Boolean enable;
    private String accessLevel;

    private List<String> tags;
    private List<AlternativeDTO> alternatives;

    private Double weight;
}

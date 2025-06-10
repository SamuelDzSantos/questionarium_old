package com.questionarium.assessment_service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private Boolean multipleChoice;
    private Integer numberLines;
    private String educationLevel;
    private String header;
    private String headerImage;
    private Long answerId;
    private Boolean enable;
    private String accessLevel;
    private List<String> tags;

    // Campos exclusivos de snapshot
    private List<AlternativeDTO> alternatives;
    private Double weight;
}

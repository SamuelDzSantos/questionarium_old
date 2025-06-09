package com.questionarium.assessment_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSnapshotDTO {
    private Long id;
    private Boolean multipleChoice;
    private Integer numberLines;
    private String educationLevel;
    private Long personId;
    private String header;
    private String headerImage;
    private Long answerId;
    private Boolean enable;
    private String accessLevel;
    private List<String> tags;
    private List<AlternativeSnapshotDTO> alternatives;
    private Double weight;
}

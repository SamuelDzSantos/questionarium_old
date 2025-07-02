package com.github.questionarium.types;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    private Long id;
    private Boolean multipleChoice;
    private Integer numberLines;
    private QuestionEducationLevel educationLevel;
    private Long userId;
    private String header;
    private String headerImage;
    private Long answerId;
    private Boolean enable;
    private QuestionAccessLevel accessLevel;
    private List<Long> tagIds;
    private List<AlternativeDTO> alternatives;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;

}

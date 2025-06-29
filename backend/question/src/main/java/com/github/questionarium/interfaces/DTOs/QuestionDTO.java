package com.github.questionarium.interfaces.DTOs;

import java.time.LocalDateTime;
import java.util.List;

import com.github.questionarium.types.QuestionAccessLevel;
import com.github.questionarium.types.QuestionEducationLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

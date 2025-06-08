package br.com.questionarium.question_service.dto;

import java.util.Set;

import br.com.questionarium.question_service.types.QuestionAccessLevel;
import br.com.questionarium.question_service.types.QuestionEducationLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Long id;
    private boolean multipleChoice;
    private Integer numberLines;
    private Long userId;
    private String header;
    private String headerImage;
    private Long answerId;
    private boolean enable;
    private QuestionEducationLevel educationLevel;
    private QuestionAccessLevel accessLevel;
    private Set<Long> tagIds;
    private Set<AlternativeDTO> alternatives;
}

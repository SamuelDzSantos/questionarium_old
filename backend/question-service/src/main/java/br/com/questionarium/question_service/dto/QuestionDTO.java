package br.com.questionarium.question_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.questionarium.question_service.types.QuestionAccessLevel;
import br.com.questionarium.question_service.types.QuestionEducationLevel;

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

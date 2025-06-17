package br.com.questionarium.question_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerKeyDTO {
    private Long questionId;
    private Long answerKey;
}

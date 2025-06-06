package br.com.questionarium.question_service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerKeyDTO implements Serializable {
    private Long questionId;
    private Long alternativeId;
}
package br.com.questionarium.question_service.dto;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AnswerKeyDTO implements Serializable{
    private Long question_id;
    private Long alternative_id;
}


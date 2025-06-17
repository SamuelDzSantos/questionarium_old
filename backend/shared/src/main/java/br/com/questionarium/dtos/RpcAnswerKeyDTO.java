package br.com.questionarium.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcAnswerKeyDTO {
        private Long questionId;
        private String answerKey;
}

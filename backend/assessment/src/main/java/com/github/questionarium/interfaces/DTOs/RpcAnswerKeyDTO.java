package com.github.questionarium.interfaces.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcAnswerKeyDTO {
        private Long questionId;
        private String answerKey;
}

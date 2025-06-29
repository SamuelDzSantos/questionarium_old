package com.github.questionarium.interfaces.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcAlternativeDTO {
        private Long alternative;
        private String description;
        private String imagePath;
        private Boolean isCorrect;
        private String explanation;
        private Integer position;
}

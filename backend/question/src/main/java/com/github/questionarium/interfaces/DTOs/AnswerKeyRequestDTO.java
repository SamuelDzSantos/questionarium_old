package com.github.questionarium.interfaces.DTOs;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerKeyRequestDTO {
    private List<Long> questionIds;
    private Long userId;
}

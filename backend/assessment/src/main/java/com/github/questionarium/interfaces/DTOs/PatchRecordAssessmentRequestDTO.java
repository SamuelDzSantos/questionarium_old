package com.github.questionarium.interfaces.DTOs;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchRecordAssessmentRequestDTO {

    @NotEmpty(message = "O gabarito do aluno não pode ser vazio")
    private List<@Size(min = 1, max = 1, message = "Cada resposta deve ser uma letra") String> studentAnswerKey;
}

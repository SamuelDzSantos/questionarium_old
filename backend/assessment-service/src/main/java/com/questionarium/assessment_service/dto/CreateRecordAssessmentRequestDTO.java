package com.questionarium.assessment_service.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateRecordAssessmentRequestDTO {
    @NotNull
    private Long appliedAssessmentId;

    /** Lista de “apelidos”/nomes dos estudantes, tamanho == quantity */
    @NotEmpty
    private List<String> studentNames;
}
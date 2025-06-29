// src/main/java/com/questionarium/assessment_service/dto/CreateRecordAssessmentRequestDTO.java
package com.github.questionarium.interfaces.DTOs;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecordAssessmentRequestDTO {
    @NotNull
    private Long appliedAssessmentId;

    @NotEmpty
    private List<String> studentNames;
}

package br.com.questionarium.assessment_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentHeaderUserDTO {
    // public AssessmentHeaderUserDTO(Long id2, String institution2, String department2, String course2, String classroom2,
    //         String professor2, LocalDate creationDate, Long userId2) {
    //     //TODO Auto-generated constructor stub
    // }
    private Long id;
    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;
    private String instructions;
    private String creationDate;
    private Long userId;
}

package com.github.questionarium.interfaces.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultDTO {

    // AppliedAssessment
    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;

    // RecordAssessment
    private String studentName;
    private Double totalScore;
    private Double obtainedScore;
    private List<String> correctAnswerKeyLetter;
    private List<String> studentAnswerKey;
}
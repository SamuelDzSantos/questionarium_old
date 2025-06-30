package com.github.questionarium.interfaces.DTOs;

public record ReportRecords(String code, String identifier, Long correctAnswers, Long incorrectAnswers,
        Boolean isGraded) {

}

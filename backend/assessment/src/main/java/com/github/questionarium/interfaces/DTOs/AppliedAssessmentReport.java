package com.github.questionarium.interfaces.DTOs;

import java.time.LocalDate;

public record AppliedAssessmentReport(Long id, LocalDate applicationDate, String classroom, String course) {

}

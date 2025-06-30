package com.github.questionarium.interfaces.DTOs;

import java.time.LocalDateTime;

public record AppliedAssessmentReport(Long id, LocalDateTime creationDateTime, String classroom, String course) {

}

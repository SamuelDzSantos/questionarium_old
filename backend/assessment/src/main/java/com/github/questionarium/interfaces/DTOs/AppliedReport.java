package com.github.questionarium.interfaces.DTOs;

import java.time.LocalDate;
import java.util.List;

public record AppliedReport(LocalDate date, String classroom, String course, Long totalRecords,
        Long correctedRecords, Long pendingRecords, List<String> tags, List<ReportRecords> records) {

}

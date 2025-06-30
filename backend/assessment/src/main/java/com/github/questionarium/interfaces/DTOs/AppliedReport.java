package com.github.questionarium.interfaces.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public record AppliedReport(LocalDateTime date, String classroom, String course, Long totalRecords,
        Long correctedRecords, Long pendingRecords, List<String> tags, List<ReportRecords> records) {

}

package com.github.questionarium.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.interfaces.DTOs.AppliedAssessmentReport;
import com.github.questionarium.interfaces.DTOs.AppliedReport;
import com.github.questionarium.interfaces.DTOs.ReportRecords;
import com.github.questionarium.model.AppliedAssessment;
import com.github.questionarium.model.QuestionSnapshot;
import com.github.questionarium.model.RecordAssessment;
import com.github.questionarium.service.AppliedAssessmentService;
import com.github.questionarium.service.RecordAssessmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final AppliedAssessmentService appliedAssessmentService;
    private final RecordAssessmentService recordAssessmentService;

    // TODO Adicionar suporte para admin ver todos users
    @GetMapping
    public List<AppliedAssessmentReport> getAppliedAssessmentList(@RequestHeader("X-User-id") Long userId) {

        return appliedAssessmentService.findByUser(userId, userId, false).stream()
                .map((assessment) -> new AppliedAssessmentReport(assessment.getId(), assessment.getCreationDateTime(),
                        assessment.getClassroom(), assessment.getCourse()))
                .toList();
    }

	@GetMapping("/assessment/{id}")
	public AppliedReport getAppliedAssessmentReport(@PathVariable Long id,
			@RequestHeader("X-User-id") Long userId) {

		AppliedAssessment appliedAssessment = appliedAssessmentService.findById(id, userId, false);
		List<RecordAssessment> records = recordAssessmentService.findByAppliedAssessment(id, userId, true);

		Set<String> uniqueTags = appliedAssessment.getQuestionSnapshots().stream()
				.map(QuestionSnapshot::getTags)
				.flatMap(List::stream)
				.collect(Collectors.toCollection(HashSet::new));

		List<ReportRecords> reportRecords = records.stream().map(record -> {
			boolean isGraded = record.getObtainedScore() != null
					|| (record.getStudentAnswerKey() != null && !record.getStudentAnswerKey().isEmpty());

			long correctCount = 0L;
			long incorrectCount = 0L;

			if (isGraded) {
				List<String> correctAnswers = record.getCorrectAnswerKeyLetter();
				List<String> studentAnswers = record.getStudentAnswerKey();

				int minSize = Math.min(correctAnswers.size(), studentAnswers.size());
				for (int i = 0; i < minSize; i++) {
					if (studentAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i))) {
						correctCount++;
					} else {
						incorrectCount++;
					}
				}

				incorrectCount += correctAnswers.size() - minSize;
			}

			return new ReportRecords(
					record.getId().toString(),
					record.getStudentName(),
					correctCount,
					incorrectCount,
					isGraded);
		}).toList();

		long totalCorrectAnswers = reportRecords.stream()
				.filter(ReportRecords::isGraded)
				.mapToLong(ReportRecords::correctAnswers)
				.sum();

		long totalIncorrectAnswers = reportRecords.stream()
				.filter(ReportRecords::isGraded)
				.mapToLong(ReportRecords::incorrectAnswers)
				.sum();

		return new AppliedReport(
				appliedAssessment.getApplicationDate(),
				appliedAssessment.getClassroom(),
				appliedAssessment.getCourse(),
				(long) records.size(),
				totalCorrectAnswers,
				totalIncorrectAnswers,
				new ArrayList<>(uniqueTags),
				reportRecords);
	}



}

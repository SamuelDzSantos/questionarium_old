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
                .map((question) -> question.getTags()).flatMap(List::stream)
                .collect(Collectors.toCollection(HashSet::new));

        System.out.println(uniqueTags);

        // TODO 0L quantidade de questões corretas ,
        List<ReportRecords> reportRecords = records.stream()
                .map((record) -> new ReportRecords(record.getId().toString(), record.getStudentName(), 0L, 0L,
                        true))
                .toList();

        return new AppliedReport(appliedAssessment.getCreationDateTime(), appliedAssessment.getClassroom(),
                appliedAssessment.getCourse(), (long) records.size(), (long) records.size(), 0L,
                new ArrayList<>(uniqueTags), reportRecords);
    }

}

package com.github.questionarium.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.service.AppliedAssessmentService;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final AppliedAssessmentService appliedAssessmentService;

    // TODO Adicionar suporte para admin ver todos users
    @GetMapping
    public String getAppliedAssessmentList(@RequestHeader("X-User-id") Long userId) {

        return "";
    }

}

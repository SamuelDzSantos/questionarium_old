package br.com.questionarium.assessment_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.assessment_service.dto.AppliedAssessmentRequest;
import br.com.questionarium.assessment_service.models.AppliedAssessment;
import br.com.questionarium.assessment_service.services.AppliedAssessmentService;

@RestController
@RequestMapping("applied-assessment")
public class AppliedAssessmentController {

    @Autowired
    private AppliedAssessmentService appliedAssessmentService;

    @PostMapping("/{assessmentId}")
    public ResponseEntity<AppliedAssessment> createAppliedAssessment(
            @PathVariable Long assessmentId,
            @RequestBody AppliedAssessmentRequest request) {
        AppliedAssessment appliedAssessment = appliedAssessmentService.createAppliedAssessment(
                assessmentId, // ID capturado da URL
                request.getQuantity(),
                request.getApplicationDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(appliedAssessment);
    }
}

package br.com.questionarium.assessment_service.controller;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<AppliedAssessment>> getAllAppliedAssessments() {
        List<AppliedAssessment> appliedAssessments = appliedAssessmentService.getAllAppliedAssessments();
        return ResponseEntity.status(HttpStatus.OK).body(appliedAssessments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppliedAssessment> getAppliedAssessmentById(@PathVariable Long id) {
        AppliedAssessment appliedAssessment = appliedAssessmentService.getAppliedAssessmentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(appliedAssessment);
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<List<AppliedAssessment>> getAllAppliedAssessmentsByUserId(@PathVariable Long userId) {
        List<AppliedAssessment> appliedAssessments = appliedAssessmentService.getAllAppliedAssessmentsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(appliedAssessments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppliedAssessment>> getAllActiveAppliedAssessmentsByUserId(@PathVariable Long userId) {
        List<AppliedAssessment> activeAssessments = appliedAssessmentService
                .getAllActiveAppliedAssessmentsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(activeAssessments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppliedAssessment> disableAppliedAssessment(@PathVariable Long id) {
        AppliedAssessment appliedAssessment = appliedAssessmentService.disableAppliedAssessment(id);
        return ResponseEntity.status(HttpStatus.OK).body(appliedAssessment);
    }

}

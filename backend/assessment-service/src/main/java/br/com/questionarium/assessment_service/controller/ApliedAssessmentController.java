package br.com.questionarium.assessment_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.assessment_service.dto.ApliedAssessmentRequest;
import br.com.questionarium.assessment_service.models.ApliedAssessment;
import br.com.questionarium.assessment_service.services.ApliedAssessmentService;

@RestController
@RequestMapping("applied-assessment")
public class ApliedAssessmentController {

    @Autowired
    private ApliedAssessmentService apliedAssessmentService;

    @PostMapping("/{assessmentId}")
    public ResponseEntity<ApliedAssessment> createApliedAssessment(
            @PathVariable Long assessmentId,
            @RequestBody ApliedAssessmentRequest request) {
        ApliedAssessment apliedAssessment = apliedAssessmentService.createApliedAssessment(
                assessmentId, // ID capturado da URL
                request.getQuantity(),
                request.getApplicationDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(apliedAssessment);
    }
}

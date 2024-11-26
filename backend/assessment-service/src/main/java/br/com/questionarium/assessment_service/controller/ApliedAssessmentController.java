package br.com.questionarium.assessment_service.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.assessment_service.models.ApliedAssessment;
import br.com.questionarium.assessment_service.models.Assessment;
import br.com.questionarium.assessment_service.services.ApliedAssessmentService;
import br.com.questionarium.assessment_service.services.AssessmentService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("applied-assessment")
public class ApliedAssessmentController {

    @Autowired
    private ApliedAssessmentService apliedAssessmentService;

    @Autowired
    private AssessmentService assessmentService;

    // CRIA UMA NOVA APLICACAO DE AVALIACAO
    @PostMapping("/{assessmentId}")
    public ResponseEntity<ApliedAssessment> createApliedAssessment(
            @PathVariable Long assessmentId,
            @RequestBody ApliedAssessment apliedAssessment) {

        // Busca o modelo de avaliação
        Assessment assessment = assessmentService.getAssessmentById(assessmentId);

        if (assessment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Cria a nova aplicação de avaliação com base no modelo
        ApliedAssessment newAppliedAssessment = apliedAssessmentService.createApliedAssessment(assessment,
                apliedAssessment);

        return new ResponseEntity<>(newAppliedAssessment, HttpStatus.CREATED);
    }

    // BUSCA UMA APLICACAO DE AVALIACAO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ApliedAssessment> getAppliedAssessmentById(@PathVariable Long id) {
        ApliedAssessment appliedAssessment = apliedAssessmentService.getApliedAssessmentById(id);
        if (appliedAssessment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(appliedAssessment);
    }

    // ALTERA O STATUS DE UMA APLICACAO DE AVALIACAO PARA FALSE (DESATIVAR)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> statusUpdate) {
        Boolean status = statusUpdate.get("status");

        if (status == null) {
            return ResponseEntity.badRequest().build(); // Retorna 400 se o status não for enviado
        }

        try {
            apliedAssessmentService.updateStatus(id, status);
            return ResponseEntity.noContent().build(); // Retorna 204 (No Content)
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 se o recurso não for encontrado
        }
    }
}

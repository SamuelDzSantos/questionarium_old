package br.com.questionarium.assessment_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.assessment_service.models.Assessment;
import br.com.questionarium.assessment_service.services.AssessmentService;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    // CRIAR AVALIACAO
    @PostMapping
    public ResponseEntity<Assessment> createAssessment(@RequestBody Assessment assessment) {
        Assessment createdAssessment = assessmentService.createAssessment(assessment);
        return new ResponseEntity<>(createdAssessment, HttpStatus.CREATED);
    }

    // LISTAR TODAS AS AVALIACOES
    @GetMapping
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        List<Assessment> assessments = assessmentService.getAllAssessments();
        return new ResponseEntity<>(assessments, HttpStatus.OK);
    }

    // BUSCAR UMA AVALIACAO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Assessment> getAssessmentById(@PathVariable Long id) {
        Assessment assessment = assessmentService.getAssessmentById(id);
        return new ResponseEntity<>(assessment, HttpStatus.OK);
    }

    // LISTAR TODAS AS AVALIACOES DE UM USUARIO
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Assessment>> getAllAssessmentsByUserId(@PathVariable Long userId) {
        List<Assessment> assessments = assessmentService.getAllAssessmentsByUserId(userId);
        return new ResponseEntity<>(assessments, HttpStatus.OK);
    }

    // DELETA AVALIACAO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ADICIONA QUESTAO NO FINAL DA LISTA DE QUESTOES DE UMA AVALIACAO
    @PutMapping("/{assessmentId}/questions/add/{questionId}")
    public ResponseEntity<Assessment> addQuestion(@PathVariable Long assessmentId, @PathVariable Long questionId) {
        Assessment updatedAssessment = assessmentService.addQuestion(assessmentId, questionId);
        return new ResponseEntity<>(updatedAssessment, HttpStatus.OK);
    }

    // REMOVE QUESTAO DA LISTA DE QUESTOES DE UMA AVALIACAO
    @PutMapping("/{assessmentId}/questions/remove/{questionId}")
    public ResponseEntity<Assessment> removeQuestion(@PathVariable Long assessmentId, @PathVariable Long questionId) {
        Assessment updatedAssessment = assessmentService.removeQuestion(assessmentId, questionId);
        return new ResponseEntity<>(updatedAssessment, HttpStatus.OK);
    }

    // ALTERA ORDEM DAS QUESTOES NA LISTA DE QUESTOES DE UMA AVALIACAO
    @PatchMapping("/{id}/reorder")
    public ResponseEntity<Assessment> reorderQuestions(@PathVariable Long id, @RequestBody List<Long> questions) {
        Assessment updatedAssessment = assessmentService.reorderQuestions(id, questions);
        return ResponseEntity.ok(updatedAssessment);
    }

    // ALTERA O CABECALHO DE UMA AVALIACAO
    @PutMapping("/{assessmentId}/header/{newHeaderId}")
    public ResponseEntity<Assessment> updateHeaderId(@PathVariable Long assessmentId, @PathVariable Long newHeaderId) {
        Assessment updatedAssessment = assessmentService.updateHeaderId(assessmentId, newHeaderId);
        return new ResponseEntity<>(updatedAssessment, HttpStatus.OK);
    }

}

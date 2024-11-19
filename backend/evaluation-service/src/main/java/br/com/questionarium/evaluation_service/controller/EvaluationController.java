package br.com.questionarium.evaluation_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.evaluation_service.models.Evaluation;
import br.com.questionarium.evaluation_service.services.EvaluationService;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    // CRIAR AVALIACAO
    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody Evaluation evaluation) {
        Evaluation createdEvaluation = evaluationService.createEvaluation(evaluation);
        return new ResponseEntity<>(createdEvaluation, HttpStatus.CREATED);
    }

    // LISTAR TODAS AS AVALIACOES
    @GetMapping
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        return new ResponseEntity<>(evaluations, HttpStatus.OK);
    }

    // BUSCAR UMA AVALIACAO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long id) {
        Evaluation evaluation = evaluationService.getEvaluationById(id);
        return new ResponseEntity<>(evaluation, HttpStatus.OK);
    }

    // LISTAR TODAS AS AVALIACOES DE UM USUARIO
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Evaluation>> getAllEvaluationsByUserId(@PathVariable Long userId) {
        List<Evaluation> evaluations = evaluationService.getAllEvaluationsByUserId(userId);
        return new ResponseEntity<>(evaluations, HttpStatus.OK);
    }

    // DELETA AVALIACAO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ADICIONA QUESTAO NO FINAL DA LISTA DE QUESTOES DE UMA AVALIACAO
    @PutMapping("/{evaluationId}/questions/add/{questionId}")
    public ResponseEntity<Evaluation> addQuestion(@PathVariable Long evaluationId, @PathVariable Long questionId) {
        Evaluation updatedEvaluation = evaluationService.addQuestion(evaluationId, questionId);
        return new ResponseEntity<>(updatedEvaluation, HttpStatus.OK);
    }

    // REMOVE QUESTAO DA LISTA DE QUESTOES DE UMA AVALIACAO
    @PutMapping("/{evaluationId}/questions/remove/{questionId}")
    public ResponseEntity<Evaluation> removeQuestion(@PathVariable Long evaluationId, @PathVariable Long questionId) {
        Evaluation updatedEvaluation = evaluationService.removeQuestion(evaluationId, questionId);
        return new ResponseEntity<>(updatedEvaluation, HttpStatus.OK);
    }

    // ALTERA ORDEM DAS QUESTOES NA LISTA DE QUESTOES DE UMA AVALIACAO
    @PatchMapping("/{id}/reorder")
    public ResponseEntity<Evaluation> reorderQuestions(@PathVariable Long id, @RequestBody List<Long> questions) {
        Evaluation updatedEvaluation = evaluationService.reorderQuestions(id, questions);
        return ResponseEntity.ok(updatedEvaluation);
    }

    // ALTERA O CABECALHO DE UMA AVALIACAO
    @PutMapping("/{evaluationId}/header/{newHeaderId}")
    public ResponseEntity<Evaluation> updateHeaderId(@PathVariable Long evaluationId, @PathVariable Long newHeaderId) {
        Evaluation updatedEvaluation = evaluationService.updateHeaderId(evaluationId, newHeaderId);
        return new ResponseEntity<>(updatedEvaluation, HttpStatus.OK);
    }

}

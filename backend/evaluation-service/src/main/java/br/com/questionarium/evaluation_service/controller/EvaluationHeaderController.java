package br.com.questionarium.evaluation_service.controller;

import br.com.questionarium.evaluation_service.models.EvaluationHeader;
import br.com.questionarium.evaluation_service.services.EvaluationHeaderService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/header")
public class EvaluationHeaderController {

    @Autowired
    private EvaluationHeaderService evaluationHeaderService;

    // CRIAR CABECALHO
    @PostMapping
    public ResponseEntity<EvaluationHeader> createHeader(@RequestBody EvaluationHeader header) {
        EvaluationHeader createdHeader = evaluationHeaderService.createHeader(header);
        return new ResponseEntity<>(createdHeader, HttpStatus.CREATED);
    }

    // MOSTRA 1 CABECALHO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationHeader> getHeaderById(@PathVariable Long id) {
        EvaluationHeader header = evaluationHeaderService.getHeaderById(id);
        if (header == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(header, HttpStatus.OK);
    }

    // MOSTRA TODOS OS CABECALHOS DE 1 USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EvaluationHeader>> getAllHeadersForUser(@PathVariable Long userId) {
        List<EvaluationHeader> headers = evaluationHeaderService.getAllHeadersByUser(userId);
        if (headers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    // ALTERAR UM CABECALHO POR ID
    @PutMapping("/{id}")
    public ResponseEntity<EvaluationHeader> updateHeader(@PathVariable Long id, @RequestBody EvaluationHeader header) {
        EvaluationHeader updatedHeader = evaluationHeaderService.updateHeader(id, header);
        if (updatedHeader == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedHeader, HttpStatus.OK);
    }

    // DELETA 1 CABECALHO POR ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id) {
        try {
            evaluationHeaderService.deleteHeader(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
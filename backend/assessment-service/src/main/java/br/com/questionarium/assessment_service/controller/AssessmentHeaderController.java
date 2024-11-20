package br.com.questionarium.assessment_service.controller;

import br.com.questionarium.assessment_service.dto.AssessmentHeaderUserDTO;
import br.com.questionarium.assessment_service.models.AssessmentHeader;
import br.com.questionarium.assessment_service.services.AssessmentHeaderService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/header")
public class AssessmentHeaderController {

    @Autowired
    private AssessmentHeaderService assessmentHeaderService;

    // CRIAR CABECALHO
    @PostMapping
    public ResponseEntity<AssessmentHeader> createHeader(@RequestBody AssessmentHeader header) {
        AssessmentHeader createdHeader = assessmentHeaderService.createHeader(header);
        return new ResponseEntity<>(createdHeader, HttpStatus.CREATED);
    }

    // MOSTRA 1 CABECALHO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentHeader> getHeaderById(@PathVariable Long id) {
        AssessmentHeader header = assessmentHeaderService.getHeaderById(id);
        if (header == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(header, HttpStatus.OK);
    }

    // MOSTRA TODOS OS CABECALHOS DE 1 USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssessmentHeaderUserDTO>> getAllHeadersForUser(@PathVariable Long userId) {
        List<AssessmentHeaderUserDTO> headers = assessmentHeaderService.getAllHeadersByUser(userId);
        if (headers == null || headers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    // ALTERAR UM CABECALHO POR ID
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentHeader> updateHeader(@PathVariable Long id, @RequestBody AssessmentHeader header) {
        AssessmentHeader updatedHeader = assessmentHeaderService.updateHeader(id, header);
        if (updatedHeader == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedHeader, HttpStatus.OK);
    }

    // DELETA 1 CABECALHO POR ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id) {
        try {
            assessmentHeaderService.deleteHeader(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
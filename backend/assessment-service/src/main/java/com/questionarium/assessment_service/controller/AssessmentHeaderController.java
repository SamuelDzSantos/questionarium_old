package com.questionarium.assessment_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.model.AssessmentHeader;
import com.questionarium.assessment_service.service.AssessmentHeaderService;

@RestController
@RequestMapping("/header")
public class AssessmentHeaderController {

    @Autowired
    private AssessmentHeaderService assessmentHeaderService;

    // CRIA HEADER
    @PostMapping
    public ResponseEntity<AssessmentHeader> createHeader(@RequestBody AssessmentHeader header) {
        AssessmentHeader createdHeader = assessmentHeaderService.createHeader(header);
        return ResponseEntity.ok(createdHeader);
    }

    // BUSCA HEADER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentHeader> getHeaderById(@PathVariable Long id) {
        Optional<AssessmentHeader> header = assessmentHeaderService.getHeaderById(id);
        return header.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // BUSCA HEADERS POR USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssessmentHeader>> getHeadersByUserId(@PathVariable Long userId) {
        List<AssessmentHeader> headers = assessmentHeaderService.getHeadersByUserId(userId);
        return ResponseEntity.ok(headers);
    }

    // BUSCA TODOS OS HEADERS
    @GetMapping
    public ResponseEntity<List<AssessmentHeader>> getAllHeaders() {
        List<AssessmentHeader> headers = assessmentHeaderService.getAllHeaders();
        return ResponseEntity.ok(headers);
    }

    // ATUALIZA HEADER
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentHeader> updateHeader(@PathVariable Long id,
            @RequestBody AssessmentHeader updatedHeader) {
        Optional<AssessmentHeader> result = assessmentHeaderService.updateHeader(id, updatedHeader);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETA HEADER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id) {
        assessmentHeaderService.deleteHeader(id);
        return ResponseEntity.noContent().build();
    }

}

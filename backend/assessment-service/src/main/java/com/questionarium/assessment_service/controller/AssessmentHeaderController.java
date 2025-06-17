package com.questionarium.assessment_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.model.AssessmentHeader;
import com.questionarium.assessment_service.security.JwtUtils;
import com.questionarium.assessment_service.service.AssessmentHeaderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/assessment-headers")
@RequiredArgsConstructor
@Slf4j
public class AssessmentHeaderController {

    private final AssessmentHeaderService assessmentHeaderService;
    private final JwtUtils jwtUtils;

    /** Cria header */
    @PostMapping
    public ResponseEntity<AssessmentHeader> createHeader(@RequestBody AssessmentHeader header) {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("POST /assessment-headers – criando AssessmentHeader para userId={}", userId);
        header.setUserId(userId);
        AssessmentHeader created = assessmentHeaderService.createHeader(header);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    /** Busca header por ID */
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentHeader> getHeaderById(@PathVariable Long id) {
        log.info("GET /assessment-headers/{} – buscando header", id);
        AssessmentHeader header = assessmentHeaderService.getHeaderById(id);
        return ResponseEntity.ok(header);
    }

    /** Busca headers do usuário logado */
    @GetMapping("/user")
    public ResponseEntity<List<AssessmentHeader>> getHeadersByUser() {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("GET /assessment-headers/user – buscando headers para userId={}", userId);
        List<AssessmentHeader> headers = assessmentHeaderService.getHeadersByUser();
        return ResponseEntity.ok(headers);
    }

    /** Busca todos os headers (admin) */
    @GetMapping
    public ResponseEntity<List<AssessmentHeader>> getAllHeaders() {
        log.info("GET /assessment-headers – buscando todos os headers (admin)");
        List<AssessmentHeader> headers = assessmentHeaderService.getAllHeaders();
        return ResponseEntity.ok(headers);
    }

    /** Atualiza um header */
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentHeader> updateHeader(
            @PathVariable Long id,
            @RequestBody AssessmentHeader updatedHeader) {
        log.info("PUT /assessment-headers/{} – atualizando header", id);
        AssessmentHeader result = assessmentHeaderService.updateHeader(id, updatedHeader);
        return ResponseEntity.ok(result);
    }

    /** Deleta um header */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id) {
        log.info("DELETE /assessment-headers/{} – deletando header", id);
        assessmentHeaderService.deleteHeader(id);
        return ResponseEntity.noContent().build();
    }
}

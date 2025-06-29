package com.github.questionarium.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.questionarium.model.AssessmentHeader;
import com.github.questionarium.service.AssessmentHeaderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/assessment-headers")
@RequiredArgsConstructor
@Slf4j
public class AssessmentHeaderController {

    private final AssessmentHeaderService assessmentHeaderService;

    /** Cria header */
    @PostMapping
    public ResponseEntity<AssessmentHeader> createHeader(@RequestBody AssessmentHeader header,
            @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("POST /assessment-headers – criando AssessmentHeader para userId={}", userId);
        header.setUserId(userId);
        AssessmentHeader created = assessmentHeaderService.createHeader(header, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    /** Busca header por ID */
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentHeader> getHeaderById(@PathVariable Long id,
            @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("GET /assessment-headers/{} – buscando header", id);
        AssessmentHeader header = assessmentHeaderService.getHeaderById(id, userId, isAdmin);
        return ResponseEntity.ok(header);
    }

    /** Busca headers do usuário logado */
    @GetMapping("/user")
    public ResponseEntity<List<AssessmentHeader>> getHeadersByUser(@RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("GET /assessment-headers/user – buscando headers para userId={}", userId);
        List<AssessmentHeader> headers = assessmentHeaderService.getHeadersByUser(userId);
        return ResponseEntity.ok(headers);
    }

    /** Busca todos os headers (admin) */
    @GetMapping
    public ResponseEntity<List<AssessmentHeader>> getAllHeaders(@RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("GET /assessment-headers – buscando todos os headers (admin)");
        List<AssessmentHeader> headers = assessmentHeaderService.getAllHeaders(isAdmin);
        return ResponseEntity.ok(headers);
    }

    /** Atualiza um header */
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentHeader> updateHeader(
            @PathVariable Long id,
            @RequestBody AssessmentHeader updatedHeader, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("PUT /assessment-headers/{} – atualizando header", id);
        AssessmentHeader result = assessmentHeaderService.updateHeader(id, updatedHeader, userId, isAdmin);
        return ResponseEntity.ok(result);
    }

    /** Deleta um header */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("DELETE /assessment-headers/{} – deletando header", id);
        assessmentHeaderService.deleteHeader(id, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }
}

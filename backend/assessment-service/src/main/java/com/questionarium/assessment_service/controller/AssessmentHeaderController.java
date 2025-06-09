package com.questionarium.assessment_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.model.AssessmentHeader;
import com.questionarium.assessment_service.security.JwtUtils;
import com.questionarium.assessment_service.service.AssessmentHeaderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/header")
@RequiredArgsConstructor
@Slf4j
public class AssessmentHeaderController {

    private final AssessmentHeaderService assessmentHeaderService;
    private final JwtUtils jwtUtils;

    /** Cria header */
    @PostMapping
    public ResponseEntity<AssessmentHeader> createHeader(@RequestBody AssessmentHeader header) {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("POST /header – criando AssessmentHeader para userId={}", userId);

        header.setUserId(userId); // força a ser o usuário do token

        AssessmentHeader createdHeader = assessmentHeaderService.createHeader(header);
        return ResponseEntity.ok(createdHeader);
    }

    /** Busca header por ID */
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentHeader> getHeaderById(@PathVariable Long id) {
        log.info("Requisição para buscar AssessmentHeader com id {}", id);
        AssessmentHeader header = assessmentHeaderService.getHeaderById(id);
        return ResponseEntity.ok(header);
    }

    /** Busca headers por userId */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssessmentHeader>> getHeadersByUserId(@PathVariable Long userId) {
        log.info("Requisição para buscar AssessmentHeaders do usuário {}", userId);
        List<AssessmentHeader> headers = assessmentHeaderService.getHeadersByUserId(userId);
        return ResponseEntity.ok(headers);
    }

    /** Busca todos os headers */
    @GetMapping
    public ResponseEntity<List<AssessmentHeader>> getAllHeaders() {
        log.info("Requisição para buscar todos os AssessmentHeaders");
        List<AssessmentHeader> headers = assessmentHeaderService.getAllHeaders();
        return ResponseEntity.ok(headers);
    }

    /** Atualiza um header */
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentHeader> updateHeader(@PathVariable Long id,
            @RequestBody AssessmentHeader updatedHeader) {
        log.info("Requisição para atualizar AssessmentHeader com id {}", id);
        AssessmentHeader result = assessmentHeaderService.updateHeader(id, updatedHeader);
        return ResponseEntity.ok(result);
    }

    /** Deleta um header */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id) {
        log.info("Requisição para deletar AssessmentHeader com id {}", id);
        assessmentHeaderService.deleteHeader(id);
        return ResponseEntity.noContent().build();
    }
}

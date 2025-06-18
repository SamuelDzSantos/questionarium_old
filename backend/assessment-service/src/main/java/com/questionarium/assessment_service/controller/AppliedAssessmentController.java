package com.questionarium.assessment_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.dto.AppliedAssessmentDTO;
import com.questionarium.assessment_service.dto.ApplyAssessmentRequestDTO;
import com.questionarium.assessment_service.mapper.AppliedAssessmentMapper;
import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.security.JwtTokenDecoder;
import com.questionarium.assessment_service.service.AppliedAssessmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/applied-assessments")
@RequiredArgsConstructor
@Slf4j
public class AppliedAssessmentController {

    private final AppliedAssessmentService service;
    private final AppliedAssessmentMapper mapper;
    private final JwtTokenDecoder jwtUtils;

    /** 1) Aplica um template e retorna o DTO criado (201 Created) */
    @PostMapping
    public ResponseEntity<AppliedAssessmentDTO> apply(
            @RequestBody @Valid ApplyAssessmentRequestDTO dto) {

        Long userId = jwtUtils.getCurrentUserId();
        log.info("POST /applied-assessments – userId={} aplicando modelo {}", userId, dto.getModelId());

        AppliedAssessment applied = service.applyAssessment(
                dto.getModelId(),
                dto.getApplicationDate(),
                dto.getQuantity(),
                dto.getShuffleQuestions());

        AppliedAssessmentDTO out = mapper.toDto(applied);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(out);
    }

    /** 2) Busca uma aplicação por ID (200 OK ou 404) */
    @GetMapping("/{id}")
    public ResponseEntity<AppliedAssessmentDTO> getOne(@PathVariable Long id) {
        log.info("GET /applied-assessments/{} – buscando aplicação", id);
        AppliedAssessment applied = service.findById(id);
        return ResponseEntity.ok(mapper.toDto(applied));
    }

    /** 3) Lista todas as aplicações (admin vê tudo, user só as ativas) */
    @GetMapping
    public ResponseEntity<List<AppliedAssessmentDTO>> listAll() {
        log.info("GET /applied-assessments – listando aplicações");
        List<AppliedAssessmentDTO> list = service.findAllActive().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /** 4) Lista aplicações do usuário logado (sempre ativas) */
    @GetMapping("/user")
    public ResponseEntity<List<AppliedAssessmentDTO>> listByUser() {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("GET /applied-assessments/user – listando aplicações de userId={}", userId);
        List<AppliedAssessmentDTO> list = service.findByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /** 5) Inativação (soft delete) de uma aplicação (204 No Content) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /applied-assessments/{} – soft-delete", id);
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}

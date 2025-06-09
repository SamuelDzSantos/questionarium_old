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
import com.questionarium.assessment_service.service.AppliedAssessmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/applied-assessment")
@RequiredArgsConstructor
@Slf4j
public class AppliedAssessmentController {

    private final AppliedAssessmentService service;
    private final AppliedAssessmentMapper mapper;

    /** 1) Aplica um template e retorna o DTO criado (201 Created) */
    @PostMapping
    public ResponseEntity<AppliedAssessmentDTO> apply(
            @RequestBody @Valid ApplyAssessmentRequestDTO dto) {

        log.info("Requisição para aplicar avaliação do modelo {}", dto.getModelId());

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
        log.info("Requisição para buscar AppliedAssessment com id {}", id);
        AppliedAssessment applied = service.findById(id);
        return ResponseEntity.ok(mapper.toDto(applied));
    }

    /** 3) Lista todas as aplicações ativas (200 OK) */
    @GetMapping
    public ResponseEntity<List<AppliedAssessmentDTO>> listAll() {
        log.info("Requisição para listar todas as AppliedAssessments ativas");
        List<AppliedAssessmentDTO> list = service.findAllActive().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /** 4) Lista aplicações ativas de um usuário (200 OK) */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppliedAssessmentDTO>> getByUser(
            @PathVariable Long userId) {
        log.info("Requisição para listar AppliedAssessments do usuário {}", userId);
        List<AppliedAssessmentDTO> list = service.findByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /** 5) Inativação (soft delete) de uma aplicação (204 No Content) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Requisição para inativar (soft-delete) AppliedAssessment com id {}", id);
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}

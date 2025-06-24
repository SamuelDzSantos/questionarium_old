package com.questionarium.assessment_service.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;


import com.questionarium.assessment_service.dto.AssessmentModelDTO;
import com.questionarium.assessment_service.dto.CreateAssessmentModelRequestDTO;
import com.questionarium.assessment_service.mapper.AssessmentModelMapper;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.security.JwtTokenDecoder;
import com.questionarium.assessment_service.service.AssessmentModelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/assessment-models")
@RequiredArgsConstructor
@Slf4j
public class AssessmentModelController {

    private final AssessmentModelService service;
    private final AssessmentModelMapper mapper;
    private final JwtTokenDecoder jwtUtils;

    /** Cria um novo modelo para o usuário logado */
    @PostMapping
    public ResponseEntity<AssessmentModelDTO> createAssessment(
            @RequestBody @Valid CreateAssessmentModelRequestDTO dto) {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("POST /assessment-models – criando AssessmentModel para userId={}", userId);

        AssessmentModel entity = mapper.toEntity(dto);

        AssessmentModel saved = service.createAssessment(entity);
        AssessmentModelDTO response = mapper.toDto(saved);
        URI location = URI.create("/assessment-models/" + saved.getId());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    /** Busca um modelo por ID */
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentModelDTO> getAssessmentById(
            @PathVariable Long id) {
        log.info("GET /assessment-models/{} – buscando modelo", id);
        AssessmentModel model = service.getAssessmentById(id);
        return ResponseEntity.ok(mapper.toDto(model));
    }

    /** Busca todos os modelos (admin) */
    @GetMapping
    public ResponseEntity<List<AssessmentModelDTO>> getAllAssessments() {
        log.info("GET /assessment-models – buscando todos os modelos");
        List<AssessmentModelDTO> dtos = service.getAllAssessments().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Busca modelos do usuário logado */
    @GetMapping("/user")
    public ResponseEntity<List<AssessmentModelDTO>> getAssessmentsByUser() {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("GET /assessment-models/user – buscando modelos de userId={}", userId);
        List<AssessmentModelDTO> dtos = service.getAssessmentsByUserId(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Atualiza um modelo */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssessmentModelDTO> updateAssessment(
            @PathVariable Long id,
            @RequestBody @Valid CreateAssessmentModelRequestDTO dto) {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("PUT /assessment-models/{} – atualizando modelo para userId={}", id, userId);

        AssessmentModel entity = mapper.toEntity(dto);
        entity.setId(id);

        AssessmentModel updated = service.updateAssessment(id, entity);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    /** Deleta um modelo */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(
            @PathVariable Long id) {
        log.info("DELETE /assessment-models/{} – deletando modelo", id);
        service.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }
}

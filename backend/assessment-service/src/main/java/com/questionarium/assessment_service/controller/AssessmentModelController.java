package com.questionarium.assessment_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.dto.AssessmentModelDTO;
import com.questionarium.assessment_service.dto.CreateAssessmentModelRequestDTO;
import com.questionarium.assessment_service.mapper.AssessmentModelMapper;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.security.JwtUtils;
import com.questionarium.assessment_service.service.AssessmentModelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/assessment")
@RequiredArgsConstructor
@Slf4j
public class AssessmentModelController {

    private final AssessmentModelService service;
    private final AssessmentModelMapper mapper;
    private final JwtUtils jwtUtils;

    /** Cria um novo modelo para o usuário logado */
    @PostMapping
    public ResponseEntity<AssessmentModelDTO> createAssessment(
            @RequestBody @Valid CreateAssessmentModelRequestDTO dto) {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("POST /assessment – criando AssessmentModel para userId={}", userId);

        AssessmentModel entity = mapper.toEntity(dto);
        entity.setUserId(userId);

        AssessmentModel saved = service.createAssessment(entity);
        return new ResponseEntity<>(mapper.toDto(saved), HttpStatus.CREATED);
    }

    /** Busca um modelo por ID */
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentModelDTO> getAssessmentById(@PathVariable Long id) {
        log.info("GET /assessment/{} – buscando modelo", id);
        AssessmentModel model = service.getAssessmentById(id);
        return ResponseEntity.ok(mapper.toDto(model));
    }

    /** Busca todos os modelos (admin) */
    @GetMapping
    public ResponseEntity<List<AssessmentModelDTO>> getAllAssessments() {
        log.info("GET /assessment – buscando todos os modelos");
        List<AssessmentModelDTO> dtos = service.getAllAssessments().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Busca modelos do usuário logado */
    @GetMapping("/user")
    public ResponseEntity<List<AssessmentModelDTO>> getAssessmentsByUser() {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("GET /assessment/user – buscando modelos de userId={}", userId);
        List<AssessmentModelDTO> dtos = service.getAssessmentsByUserId(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Atualiza um modelo */
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentModelDTO> updateAssessment(
            @PathVariable Long id,
            @RequestBody @Valid CreateAssessmentModelRequestDTO dto) {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("PUT /assessment/{} – atualizando modelo para userId={}", id, userId);

        AssessmentModel entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setUserId(userId);

        AssessmentModel updated = service.updateAssessment(id, entity);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    /** Deleta um modelo */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        log.info("DELETE /assessment/{} – deletando modelo", id);
        service.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }
}

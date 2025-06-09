package com.questionarium.assessment_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.dto.AssessmentModelDTO;
import com.questionarium.assessment_service.mapper.AssessmentModelMapper;
import com.questionarium.assessment_service.model.AssessmentModel;
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

    /** Cria um novo modelo */
    @PostMapping
    public ResponseEntity<AssessmentModelDTO> createAssessment(@RequestBody @Valid AssessmentModelDTO dto) {
        log.info("Requisição para criar novo AssessmentModel");
        AssessmentModel saved = service.createAssessment(mapper.toEntity(dto));
        return new ResponseEntity<>(mapper.toDto(saved), HttpStatus.CREATED);
    }

    /** Busca um modelo por ID */
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentModelDTO> getAssessmentById(@PathVariable Long id) {
        log.info("Requisição para buscar AssessmentModel com id {}", id);
        AssessmentModel model = service.getAssessmentById(id);
        return ResponseEntity.ok(mapper.toDto(model));
    }

    /** Busca todos os modelos (admin) */
    @GetMapping
    public ResponseEntity<List<AssessmentModelDTO>> getAllAssessments() {
        log.info("Requisição para buscar todos os AssessmentModels");
        List<AssessmentModelDTO> dtos = service.getAllAssessments()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Busca modelos por userId */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssessmentModelDTO>> getAssessmentsByUserId(@PathVariable Long userId) {
        log.info("Requisição para buscar AssessmentModels do usuário {}", userId);
        List<AssessmentModelDTO> dtos = service.getAssessmentsByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Atualiza um modelo */
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentModelDTO> updateAssessment(
            @PathVariable Long id,
            @RequestBody @Valid AssessmentModelDTO dto) {

        log.info("Requisição para atualizar AssessmentModel com id {}", id);

        AssessmentModel toUpdate = mapper.toEntity(dto);
        toUpdate.setId(id);

        AssessmentModel updated = service.updateAssessment(id, toUpdate);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    /** Deleta um modelo */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        log.info("Requisição para deletar AssessmentModel com id {}", id);
        service.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }
}

package com.questionarium.assessment_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.dto.AssessmentModelDTO;
import com.questionarium.assessment_service.mapper.AssessmentModelMapper;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.service.AssessmentModelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/assessment")
public class AssessmentModelController {

    @Autowired
    private AssessmentModelService service;

    @Autowired
    private AssessmentModelMapper mapper;

    // CRIA AVALIACAO
    @PostMapping
    public ResponseEntity<AssessmentModelDTO> createAssessment(@RequestBody AssessmentModelDTO dto) {
        AssessmentModel saved = service.createAssessment(mapper.toEntity(dto));
        return new ResponseEntity<>(mapper.toDto(saved), HttpStatus.CREATED);
    }

    // BUSCA AVALIACAO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentModelDTO> getAssessmentById(@PathVariable Long id) {
        return service.getAssessmentById(id)
                // converte AssessmentModel → AssessmentModelDTO
                .map(mapper::toDto)
                // se presente, OK(200) com o DTO; senão, 404
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // BUSCA TODAS AS AVALIACOES - NIVEL ADMIN
    @GetMapping
    public ResponseEntity<List<AssessmentModelDTO>> getAllAssessments() {
        List<AssessmentModel> assessments = service.getAllAssessments();

        List<AssessmentModelDTO> dtos = assessments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // BUSCA AVALIACOES POR ID DO USUARIO
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssessmentModelDTO>> getAssessmentsByUserId(@PathVariable Long userId) {
        List<AssessmentModel> assessments = service.getAssessmentsByUserId(userId);

        List<AssessmentModelDTO> dtos = assessments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // ATUALIZA AVALIACAO
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentModelDTO> updateAssessment(
            @PathVariable Long id,
            @RequestBody @Valid AssessmentModelDTO dto) {

        // converte DTO → Entity e garante o id vindo da URL
        AssessmentModel toUpdate = mapper.toEntity(dto);
        toUpdate.setId(id);

        // chama o service que já faz o merge
        return service.updateAssessment(id, toUpdate)
                // converte Entity → DTO
                .map(mapper::toDto)
                // se presente, 200 OK com DTO; senão, 404
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETA AVALIACAO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        boolean deleted = service.deleteAssessment(id);
        return deleted
                ? ResponseEntity.noContent().build() // 204
                : ResponseEntity.notFound().build(); // 404
    }
}

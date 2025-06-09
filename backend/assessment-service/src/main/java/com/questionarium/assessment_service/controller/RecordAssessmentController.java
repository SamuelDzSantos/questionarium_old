package com.questionarium.assessment_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.dto.CreateRecordAssessmentRequestDTO;
import com.questionarium.assessment_service.dto.RecordAssessmentDTO;
import com.questionarium.assessment_service.mapper.RecordAssessmentMapper;
import com.questionarium.assessment_service.mapper.RecordAssessmentPublicMapper;
import com.questionarium.assessment_service.service.RecordAssessmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/record-assessments")
@RequiredArgsConstructor
@Slf4j
public class RecordAssessmentController {

    private final RecordAssessmentService service;
    private final RecordAssessmentMapper mapper;
    private final RecordAssessmentPublicMapper publicMapper;

    @PostMapping
    public ResponseEntity<List<RecordAssessmentDTO>> createBatch(
            @RequestBody @Valid CreateRecordAssessmentRequestDTO dto) {

        log.info("Requisição para criar RecordAssessments em lote para AppliedAssessment {}",
                dto.getAppliedAssessmentId());

        var records = service.createFromAppliedAssessment(
                dto.getAppliedAssessmentId(),
                dto.getStudentNames());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(records));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordAssessmentDTO> getOne(@PathVariable Long id) {
        log.info("Requisição para buscar RecordAssessment {}", id);

        var rec = service.findById(id);
        return ResponseEntity.ok(mapper.toDto(rec));
    }

    @GetMapping
    public ResponseEntity<List<RecordAssessmentDTO>> listAll() {
        log.info("Requisição para listar todas as RecordAssessments ativas");

        return ResponseEntity.ok(mapper.toDto(service.findAllActive()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Requisição para soft-delete de RecordAssessment {}", id);

        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    /** Admin soft-delete */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDelete(@PathVariable Long id) {
        log.info("ADMIN: Requisição para soft-delete de RecordAssessment {}", id);

        service.adminSoftDelete(id);
        return ResponseEntity.noContent().build();
    }

    /** Consulta pública da RecordAssessment (mobile/futuro uso externo) */
    @GetMapping("/public/{id}")
    public ResponseEntity<?> publicGet(@PathVariable Long id) {
        log.info("Consulta pública para RecordAssessment {}", id);

        var rec = service.publicFindById(id);
        return ResponseEntity.ok(publicMapper.toDto(rec));
    }
}

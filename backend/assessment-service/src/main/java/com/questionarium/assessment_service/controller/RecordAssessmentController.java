package com.questionarium.assessment_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.questionarium.assessment_service.dto.CreateRecordAssessmentRequestDTO;
import com.questionarium.assessment_service.dto.RecordAssessmentDTO;
import com.questionarium.assessment_service.mapper.RecordAssessmentMapper;
import com.questionarium.assessment_service.service.RecordAssessmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/record-assessments")
public class RecordAssessmentController {

    private final RecordAssessmentService service;
    private final RecordAssessmentMapper mapper;

    public RecordAssessmentController(
            RecordAssessmentService service,
            RecordAssessmentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<List<RecordAssessmentDTO>> createBatch(
            @RequestBody @Valid CreateRecordAssessmentRequestDTO dto) {

        var records = service.createFromAppliedAssessment(
                dto.getAppliedAssessmentId(),
                dto.getStudentNames());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(records));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordAssessmentDTO> getOne(@PathVariable Long id) {
        var rec = service.findById(id);
        return ResponseEntity.ok(mapper.toDto(rec));
    }

    @GetMapping
    public ResponseEntity<List<RecordAssessmentDTO>> listAll() {
        return ResponseEntity.ok(mapper.toDto(service.findAllActive()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
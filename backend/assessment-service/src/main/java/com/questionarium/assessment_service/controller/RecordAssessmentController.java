package com.questionarium.assessment_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.dto.CreateRecordAssessmentRequestDTO;
import com.questionarium.assessment_service.dto.PatchRecordAssessmentRequestDTO;
import com.questionarium.assessment_service.dto.RecordAssessmentDTO;
import com.questionarium.assessment_service.dto.RecordAssessmentPublicDTO;
import com.questionarium.assessment_service.mapper.RecordAssessmentMapper;
import com.questionarium.assessment_service.mapper.RecordAssessmentPublicMapper;
import com.questionarium.assessment_service.service.RecordAssessmentService;
import com.questionarium.assessment_service.security.JwtTokenDecoder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/record-assessments")
@RequiredArgsConstructor
@Slf4j
public class RecordAssessmentController {

    private final RecordAssessmentService recordService;
    private final RecordAssessmentMapper mapper;
    private final RecordAssessmentPublicMapper publicMapper;
    private final JwtTokenDecoder jwtUtils;

    /** 1) Cria registros em lote */
    @PostMapping
    public ResponseEntity<List<RecordAssessmentDTO>> createBatch(
            @RequestBody @Valid CreateRecordAssessmentRequestDTO dto) {

        Long userId = jwtUtils.getCurrentUserId();
        log.info("POST /record-assessments – userId={} criando registros para appliedAssessmentId={}",
                userId, dto.getAppliedAssessmentId());

        List<RecordAssessmentDTO> out = mapper.toDto(
                recordService.createFromAppliedAssessment(
                        dto.getAppliedAssessmentId(),
                        dto.getStudentNames()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(out);
    }

    /** 2) Busca um registro por ID */
    @GetMapping("/{id}")
    public ResponseEntity<RecordAssessmentDTO> getOne(@PathVariable Long id) {
        log.info("GET /record-assessments/{} – buscando registro", id);
        RecordAssessmentDTO out = mapper.toDto(recordService.findById(id));
        return ResponseEntity.ok(out);
    }

    /** 3) Lista registros */
    @GetMapping
    public ResponseEntity<List<RecordAssessmentDTO>> listAll() {
        log.info("GET /record-assessments – listando registros");
        List<RecordAssessmentDTO> out = mapper.toDto(recordService.findAllActive());
        return ResponseEntity.ok(out);
    }

    /** 4) Lista registros do usuário logado */
    @GetMapping("/user")
    public ResponseEntity<List<RecordAssessmentDTO>> listByUser() {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("GET /record-assessments/user – listando registros de userId={}", userId);
        List<RecordAssessmentDTO> out = mapper.toDto(recordService.findByUser(userId));
        return ResponseEntity.ok(out);
    }

    /** 5) Soft-delete de um registro pelo usuário */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /record-assessments/{} – soft-delete pelo USER", id);
        recordService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    /** 6) Soft-delete de um registro pelo ADMIN */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDelete(@PathVariable Long id) {
        log.info("DELETE /record-assessments/admin/{} – soft-delete pelo ADMIN", id);
        recordService.adminSoftDelete(id);
        return ResponseEntity.noContent().build();
    }

    /** 7) Consulta pública de um registro */
    @GetMapping("/public/{id}")
    public ResponseEntity<RecordAssessmentPublicDTO> publicGet(@PathVariable Long id) {
        log.info("GET /record-assessments/public/{} – consulta pública", id);
        RecordAssessmentPublicDTO out = publicMapper.toDto(recordService.publicFindById(id));
        return ResponseEntity.ok(out);
    }

    /**
     * 8) PATCH /record-assessments/{id}
     * Atualiza studentAnswerKey (List<String>) e recalcula obtainedScore.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<RecordAssessmentDTO> patchStudentKey(
            @PathVariable Long id,
            @RequestBody @Valid PatchRecordAssessmentRequestDTO dto) {

        log.info("PATCH /record-assessments/{} – atualizando studentAnswerKey={}", id,
                dto.getStudentAnswerKey());

        // Calcula e persiste score junto com o novo studentAnswerKey
        double newScore = recordService.calculateScore(id, dto.getStudentAnswerKey());
        log.info("Novo obtainedScore para record {} = {}", id, newScore);

        // Retorna o DTO atualizado, já contendo studentAnswerKey e obtainedScore
        RecordAssessmentDTO out = mapper.toDto(recordService.findById(id));
        return ResponseEntity.ok(out);
    }
}

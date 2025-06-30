package com.github.questionarium.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.interfaces.DTOs.AssessmentResultDTO;
import com.github.questionarium.interfaces.DTOs.CreateRecordAssessmentRequestDTO;
import com.github.questionarium.interfaces.DTOs.PatchRecordAssessmentRequestDTO;
import com.github.questionarium.interfaces.DTOs.RecordAssessmentDTO;
import com.github.questionarium.interfaces.DTOs.RecordAssessmentPublicDTO;
import com.github.questionarium.service.RecordAssessmentService;
import com.github.questionarium.service.mappers.RecordAssessmentMapper;
import com.github.questionarium.service.mappers.RecordAssessmentPublicMapper;

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

    /** 1) Cria registros em lote */
    @PostMapping
    public ResponseEntity<List<RecordAssessmentDTO>> createBatch(
            @RequestBody @Valid CreateRecordAssessmentRequestDTO dto, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {

        log.info("POST /record-assessments – userId={} criando registros para appliedAssessmentId={}",
                userId, dto.getAppliedAssessmentId());

        List<RecordAssessmentDTO> out = mapper.toDto(
                recordService.createFromAppliedAssessment(
                        dto.getAppliedAssessmentId(),
                        dto.getStudentNames(), userId, isAdmin));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(out);
    }

    /** 2) Busca um registro por ID */
    @GetMapping("/{id}")
    public ResponseEntity<RecordAssessmentDTO> getOne(@PathVariable Long id, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("GET /record-assessments/{} – buscando registro", id);
        RecordAssessmentDTO out = mapper.toDto(recordService.findById(id, userId, isAdmin));
        return ResponseEntity.ok(out);
    }

    /** 3) Lista registros */
    @GetMapping
    public ResponseEntity<List<RecordAssessmentDTO>> listAll(@RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("GET /record-assessments – listando registros");
        List<RecordAssessmentDTO> out = mapper.toDto(recordService.findAllActive(userId, isAdmin));
        return ResponseEntity.ok(out);
    }

    /** 4) Lista registros do usuário logado */
    @GetMapping("/user")
    public ResponseEntity<List<RecordAssessmentDTO>> listByUser(@RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("GET /record-assessments/user – listando registros de userId={}", userId);
        List<RecordAssessmentDTO> out = mapper.toDto(recordService.findByUser(userId, isAdmin));
        return ResponseEntity.ok(out);
    }

    /** 5) Soft-delete de um registro pelo usuário */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("DELETE /record-assessments/{} – soft-delete pelo USER", id);
        recordService.softDelete(id, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    /** 6) Soft-delete de um registro pelo ADMIN */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDelete(@PathVariable Long id, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("DELETE /record-assessments/admin/{} – soft-delete pelo ADMIN", id);
        recordService.adminSoftDelete(id);
        return ResponseEntity.noContent().build();
    }

    /** 7) Consulta pública de um registro */
    @GetMapping("/public/{id}")
    public ResponseEntity<RecordAssessmentPublicDTO> publicGet(@PathVariable Long id,
            @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
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
            @RequestBody @Valid PatchRecordAssessmentRequestDTO dto, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {

        log.info("PATCH /record-assessments/{} – atualizando studentAnswerKey={}", id,
                dto.getStudentAnswerKey());

        // Calcula e persiste score junto com o novo studentAnswerKey
        double newScore = recordService.calculateScore(id, dto.getStudentAnswerKey(), userId, isAdmin);
        log.info("Novo obtainedScore para record {} = {}", id, newScore);

        // Retorna o DTO atualizado, já contendo studentAnswerKey e obtainedScore
        RecordAssessmentDTO out = mapper.toDto(recordService.findById(id, userId, isAdmin));
        return ResponseEntity.ok(out);
    }

    /**
     * 9) GET /record-assessments/result/{id}
     * Consulta dados da Record e Applied.
     */
    @GetMapping("/result/{id}")
    public ResponseEntity<AssessmentResultDTO> getResult(@PathVariable Long id) {
        log.info("GET /record-assessments/result/{} – resultado da avaliação", id);
        AssessmentResultDTO out = recordService.getAssessmentResult(id);
        return ResponseEntity.ok(out);
    }

}

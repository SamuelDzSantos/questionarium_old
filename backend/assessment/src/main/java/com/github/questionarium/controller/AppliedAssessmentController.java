package com.github.questionarium.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.questionarium.interfaces.DTOs.AppliedAssessmentDTO;
import com.github.questionarium.interfaces.DTOs.ApplyAssessmentRequestDTO;
import com.github.questionarium.model.AppliedAssessment;
import com.github.questionarium.service.AppliedAssessmentService;
import com.github.questionarium.service.mappers.AppliedAssessmentMapper;

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

        /** 1) Aplica um template e retorna o DTO criado (201 Created) */
        @PostMapping
        public ResponseEntity<AppliedAssessmentDTO> apply(
                        @RequestBody @Valid ApplyAssessmentRequestDTO dto, @RequestHeader("X-User-id") Long userId,
                        @RequestHeader("X-User-isAdmin") Boolean isAdmin) {

                log.info("POST /applied-assessments – userId={} aplicando modelo {}", userId, dto.getModelId());

                AppliedAssessment applied = service.applyAssessment(
                                dto.getModelId(),
                                dto.getDescription(),
                                dto.getApplicationDate(),
                                dto.getQuantity(),
                                dto.getShuffleQuestions(), userId, isAdmin);

                AppliedAssessmentDTO out = mapper.toDto(applied);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(out);
        }

        /** 2) Busca uma aplicação por ID (200 OK ou 404) */
        @GetMapping("/{id}")
        public ResponseEntity<AppliedAssessmentDTO> getOne(@PathVariable Long id,
                        @RequestHeader("X-User-id") Long userId,
                        @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
                log.info("GET /applied-assessments/{} – buscando aplicação", id);
                AppliedAssessment applied = service.findById(id, userId, isAdmin);
                return ResponseEntity.ok(mapper.toDto(applied));
        }

        /** 3) Lista todas as aplicações (admin vê tudo, user só as ativas) */
        @GetMapping
        public ResponseEntity<List<AppliedAssessmentDTO>> listAll(@RequestHeader("X-User-id") Long userId,
                        @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
                log.info("GET /applied-assessments – listando aplicações");
                List<AppliedAssessmentDTO> list = service.findAllActive(userId, isAdmin).stream()
                                .map(mapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(list);
        }

        /** 4) Lista aplicações do usuário logado (sempre ativas) */
        @GetMapping("/user")
        public ResponseEntity<List<AppliedAssessmentDTO>> listByUser(@RequestHeader("X-User-id") Long userId,
                        @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
                log.info("GET /applied-assessments/user – listando aplicações de userId={}", userId);
                List<AppliedAssessmentDTO> list = service.findByUser(userId, userId, isAdmin).stream()
                                .map(mapper::toDto)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(list);
        }

        /** 5) Inativação (soft delete) de uma aplicação (204 No Content) */
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("X-User-id") Long userId,
                        @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
                log.info("DELETE /applied-assessments/{} – soft-delete", id);
                service.softDelete(id, userId, isAdmin);
                return ResponseEntity.noContent().build();
        }

        /**
         * 6) Lista aplicações do usuário logado (sempre ativas) e filtradas por
         * descrição e data
         */
        @GetMapping("/user/filter")
        public ResponseEntity<List<AppliedAssessmentDTO>> getFilteredByUser(
                        @RequestHeader("X-User-id") Long userId,
                        @RequestHeader("X-User-isAdmin") Boolean isAdmin,
                        @RequestParam(required = false) String description,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate applicationDate) {

                log.info("GET /applied-assessments/user/filter – listando aplicações filtradas de userId={}, isAdmin={}",
                                userId, isAdmin);

                List<AppliedAssessmentDTO> list = service
                                .getFilteredAppliedAssessments(userId, isAdmin, description, applicationDate).stream()
                                .map(mapper::toDto)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(list);
        }

}

package com.github.questionarium.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.questionarium.interfaces.DTOs.AssessmentModelDTO;
import com.github.questionarium.interfaces.DTOs.CreateAssessmentModelRequestDTO;
import com.github.questionarium.model.AssessmentModel;
import com.github.questionarium.model.QuestionWeight;
import com.github.questionarium.service.AssessmentModelService;
import com.github.questionarium.service.mappers.AssessmentModelMapper;

import org.springframework.http.MediaType;

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

    /** Cria um novo modelo para o usuário logado */
    @PostMapping
    public ResponseEntity<AssessmentModelDTO> createAssessment(
            @RequestBody @Valid CreateAssessmentModelRequestDTO dto, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("POST /assessment-models – criando AssessmentModel para userId={}", userId);

        System.out.println(dto.getQuestions());

        AssessmentModel entity = mapper.toEntity(dto);

        // List<QuestionWeight> weights = dto.getQuestions().stream()
        // .map((question) -> new QuestionWeight(question.getQuestion(),
        // question.getWeight())).toList();
        List<QuestionWeight> weights = dto.getQuestions().stream()
                .map((question) -> {
                    return new QuestionWeight(question.getQuestionId(), question.getWeight(), 0);
                }).toList();

        for (Integer x = 0; x < weights.size(); x++) {
            weights.get(x).setQuestionOrder(x);
        }

        entity.setQuestions(weights);

        AssessmentModel saved = service.createAssessment(entity, userId);

        AssessmentModelDTO response = mapper.toDto(saved);

        URI location = URI.create("/assessment-models/" + saved.getId());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    /** Busca um modelo por ID */
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentModelDTO> getAssessmentById(
            @PathVariable Long id, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("GET /assessment-models/{} – buscando modelo", id);
        AssessmentModel model = service.getAssessmentById(id, userId, isAdmin);

        System.out.println("Model----------------------------------------------" + model.toString());

        return ResponseEntity.ok(mapper.toDto(model));
    }

    /** Busca todos os modelos (admin) */
    @GetMapping
    public ResponseEntity<List<AssessmentModelDTO>> getAllAssessments(@RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("GET /assessment-models – buscando todos os modelos");
        List<AssessmentModelDTO> dtos = service.getAllAssessments(isAdmin).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Busca modelos do usuário logado */
    @GetMapping("/user")
    public ResponseEntity<List<AssessmentModelDTO>> getAssessmentsByUser(@RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String institution,
            @RequestParam(required = false) String classroom,
            @RequestParam(required = false) String course) {
        System.out.println("-----------------------");
        System.out.println(description);
        log.info("GET /assessment-models/user – buscando modelos de userId={}", userId);
        List<AssessmentModelDTO> dtos = service
                .getAssessmentsByUserIdFiltered(userId, isAdmin, description, institution, classroom, course)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /** Atualiza um modelo */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssessmentModelDTO> updateAssessment(
            @PathVariable Long id,
            @RequestBody @Valid CreateAssessmentModelRequestDTO dto, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("PUT /assessment-models/{} – atualizando modelo para userId={}", id, userId);

        System.out.println(dto);

        AssessmentModel entity = mapper.toEntity(dto);
        entity.setId(id);

        AssessmentModel updated = service.updateAssessment(id, entity, userId, isAdmin);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    /** Deleta um modelo */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(
            @PathVariable Long id, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        log.info("DELETE /assessment-models/{} – deletando modelo", id);
        service.deleteAssessment(id, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }
}

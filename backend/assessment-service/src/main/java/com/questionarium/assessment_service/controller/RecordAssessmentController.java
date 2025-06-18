package com.questionarium.assessment_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.questionarium.assessment_service.dto.AppliedAssessmentDTO;
import com.questionarium.assessment_service.dto.ApplyAssessmentRequestDTO;
import com.questionarium.assessment_service.dto.CreateRecordAssessmentRequestDTO;
import com.questionarium.assessment_service.dto.RecordAssessmentDTO;
import com.questionarium.assessment_service.dto.RecordAssessmentPublicDTO;
import com.questionarium.assessment_service.mapper.RecordAssessmentMapper;
import com.questionarium.assessment_service.mapper.RecordAssessmentPublicMapper;
import com.questionarium.assessment_service.model.AppliedAssessment;
import com.questionarium.assessment_service.security.JwtUtils;
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
    private final JwtUtils jwtUtils;

    /** 1) Cria registros em lote (soft-delete apenas inativa, não remove) */
    @PostMapping
    public ResponseEntity<List<RecordAssessmentDTO>> createBatch(
            @RequestBody @Valid CreateRecordAssessmentRequestDTO dto) {

        Long userId = jwtUtils.getCurrentUserId();
        log.info("POST /record-assessments – userId={} criando registros para appliedAssessmentId={}", userId,
                dto.getAppliedAssessmentId());

        List<RecordAssessmentDTO> out = mapper.toDto(
                service.createFromAppliedAssessment(
                        dto.getAppliedAssessmentId(),
                        dto.getStudentNames()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(out);
    }

//     import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
// import org.springframework.hateoas.EntityModel;
// import org.springframework.hateoas.Link;

// @PostMapping
// public ResponseEntity<EntityModel<AppliedAssessmentDTO>> apply(
//     @Valid @RequestBody ApplyAssessmentRequestDTO dto) {

//   // 1) cria a avaliação aplicada
//   AppliedAssessment applied = service.applyAssessment(
//       dto.getModelId(),
//       dto.getApplicationDate(),
//       dto.getQuantity(),
//       dto.getShuffleQuestions()
//   );
//   AppliedAssessmentDTO body = mapper.toDto(applied);

//   // 2) monta o model HATEOAS
//   EntityModel<AppliedAssessmentDTO> model = EntityModel.of(body);

//   // link para criar records: passa só o ID, lista de nomes fica nula/vazia
//   CreateRecordAssessmentRequestDTO template = 
//       new CreateRecordAssessmentRequestDTO(applied.getId(), Collections.emptyList());
//   Link createRecords = linkTo(methodOn(RecordAssessmentController.class)
//       .createBatch(template))
//     .withRel("create-records")
//     .withTitle("Cria registros (studentNames opcional)");

//   model.add(
//     linkTo(methodOn(AppliedAssessmentController.class)
//       .getOne(applied.getId())).withSelfRel(),
//     createRecords,
//     linkTo(methodOn(RecordAssessmentController.class)
//       .listByUser()).withRel("my-records")
//   );

//   return ResponseEntity
//     .created(linkTo(methodOn(AppliedAssessmentController.class)
//       .getOne(applied.getId())).toUri())
//     .body(model);
// }



//     {
//   "description": "...",
//   "links": [
//     { "rel": "self", "href": "/applied-assessments/123" },
//     { "rel": "create-records",
//       "href": "/record-assessments",
//       "title": "Cria registros (studentNames opcional)" },
//     { "rel": "my-records", "href": "/record-assessments/user" }
//   ]
// }

    /** 2) Busca um registro por ID (admin vê qualquer, user só ativo) */
    @GetMapping("/{id}")
    public ResponseEntity<RecordAssessmentDTO> getOne(@PathVariable Long id) {
        log.info("GET /record-assessments/{} – buscando registro", id);
        RecordAssessmentDTO out = mapper.toDto(service.findById(id));
        return ResponseEntity.ok(out);
    }

    /** 3) Lista registros (admin vê todos, user só ativos) */
    @GetMapping
    public ResponseEntity<List<RecordAssessmentDTO>> listAll() {
        log.info("GET /record-assessments – listando registros");
        List<RecordAssessmentDTO> out = mapper.toDto(service.findAllActive());
        return ResponseEntity.ok(out);
    }

    /** 4) Lista registros do usuário logado (sempre só ativos) */
    @GetMapping("/user")
    public ResponseEntity<List<RecordAssessmentDTO>> listByUser() {
        Long userId = jwtUtils.getCurrentUserId();
        log.info("GET /record-assessments/user – listando registros de userId={}", userId);
        List<RecordAssessmentDTO> out = mapper.toDto(service.findByUser(userId));
        return ResponseEntity.ok(out);
    }

    /** 5) Soft-delete de um registro pelo usuário (inativa somente se for dono) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /record-assessments/{} – soft-delete pelo USER", id);
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    /** 6) Soft-delete de um registro pelo ADMIN (inativa qualquer) */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDelete(@PathVariable Long id) {
        log.info("DELETE /record-assessments/admin/{} – soft-delete pelo ADMIN", id);
        service.adminSoftDelete(id);
        return ResponseEntity.noContent().build();
    }

    /** 7) Consulta pública de um registro, sem autenticação adicional */
    @GetMapping("/public/{id}")
    public ResponseEntity<RecordAssessmentPublicDTO> publicGet(@PathVariable Long id) {
        log.info("GET /record-assessments/public/{} – consulta pública", id);
        RecordAssessmentPublicDTO out = publicMapper.toDto(service.publicFindById(id));
        return ResponseEntity.ok(out);
    }
}

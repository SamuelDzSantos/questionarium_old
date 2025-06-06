package br.com.questionarium.question_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.question_service.dto.TagDTO;
import br.com.questionarium.question_service.service.TagService;

@RestController
@RequestMapping("/questions/tags")
public class TagController {

    private static final Logger logger = LoggerFactory.getLogger(TagController.class);
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        logger.info("POST /questions/tags – criando tag com nome='{}'", tagDTO.getName());
        TagDTO createdTag = tagService.createTag(tagDTO);
        logger.info("Tag criada com ID={}", createdTag.getId());
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        logger.info("GET /questions/tags – buscando todas as tags");
        List<TagDTO> tags = tagService.getAllTags();
        logger.info("Foram encontradas {} tags", tags.size());
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
        logger.info("GET /questions/tags/{} – buscando tag por ID", id);
        try {
            TagDTO tagDTO = tagService.getTagById(id);
            logger.info("Tag {} encontrada: '{}'", id, tagDTO.getName());
            return new ResponseEntity<>(tagDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.warn("Tag não encontrada com ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        logger.info("PUT /questions/tags/{} – atualizando tag com dados {}", id, tagDTO);
        try {
            TagDTO updatedTag = tagService.updateTag(id, tagDTO);
            logger.info("Tag {} atualizada com sucesso", id);
            return new ResponseEntity<>(updatedTag, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.warn("Falha ao atualizar: tag não encontrada com ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        logger.info("DELETE /questions/tags/{} – excluindo tag", id);
        try {
            tagService.deleteTag(id);
            logger.info("Tag {} excluída com sucesso", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            logger.warn("Falha ao excluir: tag não encontrada com ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

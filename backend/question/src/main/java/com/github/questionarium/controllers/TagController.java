package com.github.questionarium.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.questionarium.interfaces.DTOs.TagDTO;
import com.github.questionarium.services.TagService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/questions/tags")
@Slf4j
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        log.info("POST /questions/tags – criando tag com nome='{}'", tagDTO.getName());
        TagDTO createdTag = tagService.createTag(tagDTO);
        log.info("Tag criada com ID={}", createdTag.getId());
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        log.info("GET /questions/tags – buscando todas as tags");
        List<TagDTO> tags = tagService.getAllTags();
        log.info("Foram encontradas {} tags", tags.size());
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
        log.info("GET /questions/tags/{} – buscando tag por ID", id);
        try {
            TagDTO tagDTO = tagService.getTagById(id);
            log.info("Tag {} encontrada: '{}'", id, tagDTO.getName());
            return new ResponseEntity<>(tagDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.warn("Tag não encontrada com ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        log.info("PUT /questions/tags/{} – atualizando tag com dados {}", id, tagDTO);
        try {
            TagDTO updatedTag = tagService.updateTag(id, tagDTO);
            log.info("Tag {} atualizada com sucesso", id);
            return new ResponseEntity<>(updatedTag, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.warn("Falha ao atualizar: tag não encontrada com ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        log.info("DELETE /questions/tags/{} – excluindo tag", id);
        try {
            tagService.deleteTag(id);
            log.info("Tag {} excluída com sucesso", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            log.warn("Falha ao excluir: tag não encontrada com ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

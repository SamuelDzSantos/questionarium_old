package com.github.questionarium.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.interfaces.DTOs.AnswerKeyDTO;
import com.github.questionarium.interfaces.DTOs.QuestionDTO;
import com.github.questionarium.services.QuestionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/questions")
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;

    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO,
            @RequestHeader("X-User-id") Long userId) {
        log.info("POST /questions – criando questão para userId={}", userId);

        questionDTO.setUserId(userId);
        QuestionDTO created = questionService.createQuestion(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getQuestions(
            @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin,
            @RequestParam(required = false) Boolean multipleChoice,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) Integer accessLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String header) {
        List<QuestionDTO> list = questionService.getFilteredQuestions(
                userId, isAdmin, multipleChoice, tagIds, accessLevel, educationLevel, header);
        System.out.println(accessLevel);
        System.out.println(tagIds);
        System.out.println(multipleChoice);
        System.out.println(educationLevel);
        System.out.println(header);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id, @RequestHeader("X-User-id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {
        return questionService.getQuestionById(id, userId, isAdmin)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/answer-key")
    public ResponseEntity<List<AnswerKeyDTO>> getAnswerKeys(@RequestParam List<Long> questionIds,
            @RequestHeader("X-User-id") Long userId) {
        List<AnswerKeyDTO> list = questionService.getAnswerKeys(questionIds, userId);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id,
            @RequestBody QuestionDTO questionDTO,
            @RequestHeader("X-User-id") Long adminId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {

        try {
            QuestionDTO updated = questionService.updateQuestion(id, questionDTO, adminId, isAdmin);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id, @RequestHeader("X-User-id") Long adminId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {

        try {
            questionService.deleteQuestion(id, adminId, isAdmin);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

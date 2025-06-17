package br.com.questionarium.question_service.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.service.QuestionService;
import br.com.questionarium.question_service.security.JwtUtils;

import jakarta.persistence.EntityNotFoundException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionService questionService;
    private final AsyncRabbitTemplate asyncRabbitTemplate;
    private final JwtUtils jwtUtils;

    public QuestionController(QuestionService questionService,
            AsyncRabbitTemplate asyncRabbitTemplate,
            JwtUtils jwtUtils) {
        this.questionService = questionService;
        this.asyncRabbitTemplate = asyncRabbitTemplate;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
        Long userId = jwtUtils.getCurrentUserId();
        logger.info("POST /questions – criando questão para userId={}", userId);

        questionDTO.setUserId(userId);
        QuestionDTO created = questionService.createQuestion(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<QuestionDTO> list = questionService.getAllQuestions();
        return ResponseEntity.ok(list);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getFilteredQuestions(
            @RequestParam(required = false) Boolean multipleChoice,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) Integer accessLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String header) {

        Long userId = jwtUtils.getCurrentUserId();
        List<QuestionDTO> list = questionService.getFilteredQuestions(
                userId, multipleChoice, tagIds, accessLevel, educationLevel, header);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/admin/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionDTO>> getFilteredQuestionsAsAdmin(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Boolean multipleChoice,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) Integer accessLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String header) {

        List<QuestionDTO> list = questionService.getFilteredQuestionsAsAdmin(
                userId, multipleChoice, tagIds, accessLevel, educationLevel, header);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        Long userId = jwtUtils.getCurrentUserId();
        return questionService.getQuestionByIdForUser(id, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionDTO> getQuestionByIdAdmin(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/answer-key")
    public ResponseEntity<List<AnswerKeyDTO>> getAnswerKeys(@RequestParam List<Long> questionIds) {
        Long userId = jwtUtils.getCurrentUserId();
        List<AnswerKeyDTO> list = questionService.getAnswerKeys(questionIds, userId);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id,
            @RequestBody QuestionDTO questionDTO) {
        Long userId = jwtUtils.getCurrentUserId();
        questionDTO.setUserId(userId);
        try {
            QuestionDTO updated = questionService.updateQuestion(id, questionDTO, userId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionDTO> updateQuestionAdmin(@PathVariable Long id,
            @RequestBody QuestionDTO questionDTO) {
        Long adminId = jwtUtils.getCurrentUserId();
        try {
            QuestionDTO updated = questionService.updateQuestion(id, questionDTO, adminId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        Long userId = jwtUtils.getCurrentUserId();
        try {
            questionService.deleteQuestion(id, userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQuestionAdmin(@PathVariable Long id) {
        Long adminId = jwtUtils.getCurrentUserId();
        try {
            questionService.deleteQuestion(id, adminId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    public Mono<Object> testRabbit() {
        CompletableFuture<Object> future = asyncRabbitTemplate
                .convertSendAndReceive("question-exchange", "question.answer-key", List.of(1L, 2L))
                .toCompletableFuture();
        return Mono.fromFuture(future);
    }
}

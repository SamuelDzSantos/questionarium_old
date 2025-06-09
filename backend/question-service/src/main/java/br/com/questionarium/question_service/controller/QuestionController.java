package br.com.questionarium.question_service.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.security.JwtUtils;
import br.com.questionarium.question_service.service.QuestionService;
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

        QuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
        logger.info("Questão criada com ID={} para userId={}", createdQuestion.getId(), userId);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        logger.info("GET /questions/all – buscando todas as questões (ADMIN)");
        List<QuestionDTO> questions = questionService.getAllQuestions();
        logger.info("Retornadas {} questões", questions.size());
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getFilteredQuestions(
            @RequestParam(required = false) Boolean multipleChoice,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) Integer accessLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String header) {

        Long userId = jwtUtils.getCurrentUserId();

        logger.info(
                "GET /questions – filtros: userId={}, multipleChoice={}, tagIds={}, accessLevel={}, educationLevel={}, header={}",
                userId, multipleChoice, tagIds, accessLevel, educationLevel, header);

        List<QuestionDTO> questions = questionService.getFilteredQuestions(
                userId, multipleChoice, tagIds, accessLevel, educationLevel, header);
        logger.info("Retornadas {} questões filtradas", questions.size());
        return new ResponseEntity<>(questions, HttpStatus.OK);
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

        logger.info("GET /questions/admin/filter – buscando questões com filtro (ADMIN)");

        List<QuestionDTO> questions = questionService.getFilteredQuestionsAsAdmin(
                userId, multipleChoice, tagIds, accessLevel, educationLevel, header);

        logger.info("Retornadas {} questões filtradas (ADMIN).", questions.size());
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        Long userId = jwtUtils.getCurrentUserId();
        logger.info("GET /questions/{} – buscando questão por ID para userId={}", id, userId);

        return questionService.getQuestionByIdForUser(id, userId)
                .map(q -> {
                    logger.info("Questão {} encontrada para userId={}", id, userId);
                    return ResponseEntity.ok(q);
                })
                .orElseGet(() -> {
                    logger.warn("Questão {} não encontrada ou não pertence a userId={}", id, userId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionDTO> getQuestionByIdAdmin(@PathVariable Long id) {
        logger.info("GET /questions/admin/{} – buscando questão por ID (ADMIN)", id);

        return questionService.getQuestionById(id)
                .map(q -> {
                    logger.info("Questão {} encontrada (ADMIN)", id);
                    return ResponseEntity.ok(q);
                })
                .orElseGet(() -> {
                    logger.warn("Questão {} não encontrada (ADMIN)", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @GetMapping("/answer-key")
    public ResponseEntity<List<AnswerKeyDTO>> getAnswerKeys(@RequestParam List<Long> questionIds) {
        Long userId = jwtUtils.getCurrentUserId();
        logger.info("GET /questions/answer-key – buscando answer keys para IDs={} e userId={}", questionIds, userId);

        List<AnswerKeyDTO> list = questionService.getAnswerKeys(questionIds, userId);

        if (list.isEmpty()) {
            logger.warn("Nenhuma answer key encontrada ou não autorizada para IDs={} e userId={}", questionIds, userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        logger.info("Answer keys retornadas: {}", list);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO) {
        Long userId = jwtUtils.getCurrentUserId();
        logger.info("PUT /questions/{} – atualizando questão para userId={}", id, userId);

        questionDTO.setUserId(userId);

        try {
            QuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO, userId);
            logger.info("Questão {} atualizada com sucesso", id);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.warn("Falha ao atualizar: questão {} não encontrada ou sem permissão", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            logger.error("Falha ao atualizar questão {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Erro interno ao atualizar questão {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionDTO> updateQuestionAdmin(@PathVariable Long id,
            @RequestBody QuestionDTO questionDTO) {
        logger.info("PUT /questions/admin/{} – atualizando questão como ADMIN", id);

        try {
            // ADMIN pode atualizar qualquer uma → NÃO passa userId para validar
            QuestionDTO updatedQuestion = questionService.updateQuestionAsAdmin(id, questionDTO);
            logger.info("Questão {} atualizada com sucesso (ADMIN)", id);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.warn("Falha ao atualizar (ADMIN): questão {} não encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            logger.error("Falha ao atualizar questão {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Erro interno ao atualizar questão {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        Long userId = jwtUtils.getCurrentUserId();
        logger.info("DELETE /questions/{} – tentativa de deletar questão para userId={}", id, userId);

        try {
            questionService.deleteQuestion(id, userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("Falha ao deletar: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro interno ao tentar deletar questão {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQuestionAsAdmin(@PathVariable Long id) {
        logger.info("DELETE /questions/admin/{} – tentativa de deletar questão como ADMIN", id);

        try {
            questionService.deleteQuestionAsAdmin(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("Falha ao deletar (ADMIN): {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Erro interno ao tentar deletar questão {} (ADMIN)", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test")
    public Object testRabbit() {
        logger.info("GET /questions/test – enviando requisição RabbitMQ para question.answer-key");
        List<Long> list = List.of(1L, 2L);

        CompletableFuture<Object> futureResponse = asyncRabbitTemplate
                .convertSendAndReceive("question-exchange", "question.answer-key", list)
                .toCompletableFuture();

        return Mono.fromFuture(futureResponse)
                .map(response -> {
                    logger.info("Resposta recebida de RabbitMQ: {}", response);
                    return response;
                })
                .onErrorResume(e -> {
                    logger.error("Erro ao obter resposta RabbitMQ", e);
                    return Mono.empty();
                });
    }
}

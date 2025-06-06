package br.com.questionarium.question_service.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionService questionService;
    private final AsyncRabbitTemplate asyncRabbitTemplate;

    public QuestionController(QuestionService questionService,
            AsyncRabbitTemplate asyncRabbitTemplate) {
        this.questionService = questionService;
        this.asyncRabbitTemplate = asyncRabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(
            @RequestBody QuestionDTO questionDTO,
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        logger.info("POST /questions – criando questão para userId={}", userId);

        // Ignora o personId que veio no DTO e sobrescreve com o ID do token
        questionDTO.setPersonId(userId);

        QuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
        logger.info("Questão criada com ID={} para userId={}", createdQuestion.getId(), userId);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        logger.info("GET /questions/all – buscando todas as questões");
        List<QuestionDTO> questions = questionService.getAllQuestions();
        logger.info("Retornadas {} questões", questions.size());
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getFilteredQuestions(
            @RequestParam(required = false) Long personId,
            @RequestParam(required = false) Boolean multipleChoice,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) Integer accessLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String header) {

        logger.info(
                "GET /questions – filtros: personId={}, multipleChoice={}, tagIds={}, accessLevel={}, educationLevel={}, header={}",
                personId, multipleChoice, tagIds, accessLevel, educationLevel, header);

        List<QuestionDTO> questions = questionService.getFilteredQuestions(
                personId, multipleChoice, tagIds, accessLevel, educationLevel, header);
        logger.info("Retornadas {} questões filtradas", questions.size());
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        logger.info("GET /questions/{} – buscando questão por ID", id);
        return questionService.getQuestionById(id)
                .map(q -> {
                    logger.info("Questão {} encontrada", id);
                    return ResponseEntity.ok(q);
                })
                .orElseGet(() -> {
                    logger.warn("Questão {} não encontrada", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    @GetMapping("/answer-key")
    public ResponseEntity<List<AnswerKeyDTO>> getAnswerKeys(@RequestParam List<Long> questionIds) {
        logger.info("GET /questions/answer-key – buscando answer keys para IDs={}", questionIds);
        List<AnswerKeyDTO> list = questionService.getAnswerKeys(questionIds);
        if (list.isEmpty()) {
            logger.warn("Nenhuma answer key encontrada para IDs={}", questionIds);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Answer keys retornadas: {}", list);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable Long id,
            @RequestBody QuestionDTO questionDTO,
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        logger.info("PUT /questions/{} – atualizando questão para userId={}", id, userId);

        // Sobrescreve personId no DTO para evitar adulteração
        questionDTO.setPersonId(userId);

        try {
            QuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO);
            logger.info("Questão {} atualizada com sucesso", id);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.warn("Falha ao atualizar: questão {} não encontrada", id);
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
        logger.info("DELETE /questions/{} – inativando questão", id);
        try {
            questionService.deleteQuestion(id);
            logger.info("Questão {} inativada com sucesso", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            logger.warn("Falha ao inativar: questão {} não encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

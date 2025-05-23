package br.com.questionarium.question_service.controller;

import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import reactor.core.publisher.Mono;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    @Autowired
    private AsyncRabbitTemplate template;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getFilteredQuestions(
            @RequestParam(required = false) Long personId,
            @RequestParam(required = false) Boolean multipleChoice,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) Integer accessLevel,
            @RequestParam(required = false) Integer educationLevel,
            @RequestParam(required = false) String header
            ) {

        List<QuestionDTO> questions = questionService.getFilteredQuestions(
                personId, multipleChoice, tagIds, accessLevel, educationLevel, header);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/answer-key")
    public ResponseEntity<List<AnswerKeyDTO>> getAnswerKeys(@RequestParam List<Long> questionIds) {
        List<AnswerKeyDTO> list = questionService.getAnswerKeys(questionIds);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO) {
        try {
            QuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("test")
    public Object test() {
        List<Integer> list = List.of(1, 2);

        CompletableFuture<Object> f = template.convertSendAndReceive("default-exchange",
                "question.answer-key",
                list)
                .toCompletableFuture();

        return Mono.fromFuture(f).map(response -> {
            try {
                System.out.println("******************");
                System.out.println(response);
                return response;
            } catch (Exception e) {
                return null;
            }
        });
    }

    @GetMapping("getbyids")
    public Object testGetByIds() {
        List<Long> list = List.of(1L, 2L);

        CompletableFuture<Object> f = template.convertSendAndReceive("default-exchange",
                "question.getbyids",
                list)
                .toCompletableFuture();

                return Mono.fromFuture(f).map(response -> {
            try {
                System.out.println("******************");
                System.out.println(response);
                return response;
            } catch (Exception e) {
                return null;
            }
        });
    }

}

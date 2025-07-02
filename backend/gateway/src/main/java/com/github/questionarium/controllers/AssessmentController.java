package com.github.questionarium.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.questionarium.types.AssessmentModelDTO;
import com.github.questionarium.types.CustomModel;
import com.github.questionarium.types.CustomQuestion;
import com.github.questionarium.types.QuestionDTO;
import com.github.questionarium.types.QuestionWeightDTO;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assessment")
public class AssessmentController {

    private final WebClient webClient;

    @GetMapping("/model/{id}")
    public Flux<CustomModel> getAssessmentModel(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-isAdmin") Boolean isAdmin) {

        Mono<AssessmentModelDTO> asssessmentMono = null;

        return webClient.get()
                .uri("http://localhost:14005/assessment-models/" + id)
                .header("X-User-Id", userId.toString()).header("X-User-isAdmin", isAdmin.toString())
                .retrieve()
                .bodyToMono(AssessmentModelDTO.class)
                .flatMapMany(assessment -> {
                    List<QuestionWeightDTO> questions = assessment.getQuestions();
                    return Flux.fromIterable(questions).flatMap(
                            q -> webClient.get().uri("http://localhost:14004/questions/" + q.getQuestionId())
                                    .header("X-User-Id", userId.toString())
                                    .header("X-User-isAdmin", isAdmin.toString())
                                    .retrieve()
                                    .bodyToMono(QuestionDTO.class)
                                    .map(question -> new CustomQuestion(question.getId(), question.getMultipleChoice(),
                                            question.getNumberLines(), question.getEducationLevel(),
                                            question.getUserId(), question.getHeader(), question.getHeaderImage(),
                                            question.getAnswerId(), question.getEnable(), question.getAccessLevel(),
                                            question.getTagIds(), question.getAlternatives(),
                                            question.getCreationDateTime(), question.getUpdateDateTime(),
                                            q.getWeight()))

                )
                            .collectList()
                            .map(q -> new CustomModel(assessment.getDescription(), assessment.getInstitution(),
                                    assessment.getDepartment(), assessment.getCourse(),
                                    assessment.getClassroom(), assessment.getProfessor(), assessment.getInstructions(),
                                    assessment.getImage(), q));
                });

    }

}

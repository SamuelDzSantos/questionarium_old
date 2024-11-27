package br.com.questionarium.question_service.init;

import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.questionarium.question_service.dto.AlternativeDTO;
import br.com.questionarium.question_service.dto.HeaderDTO;
import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.dto.TagDTO;
import br.com.questionarium.question_service.service.HeaderService;
import br.com.questionarium.question_service.service.QuestionService;
import br.com.questionarium.question_service.service.TagService;
import br.com.questionarium.question_service.types.QuestionAccessLevel;
import br.com.questionarium.question_service.types.QuestionEducationLevel;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class dataLoader implements ApplicationRunner {

    private final QuestionService questionService;
    private final TagService tagService;
    private final HeaderService headerService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        headerService.createHeader(HeaderDTO.builder()
            .content("Header")
            .imagePath("$path")
            .build()
        );

        tagService.createTag(TagDTO.builder()
            .name("TI")
            .description("Tecnologia da Informação")
            .build());

        questionService.createQuestion(QuestionDTO.builder()
            .multipleChoice(true)
            .numberLines(15)
            .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
            .personId(99L)
            .headerId(1L)
            .answerId(0L)
            .enable(true)
            .accessLevel(QuestionAccessLevel.PRIVATE)
            .tagIds(Set.of(1L))
            .alternatives(Set.of(
                new AlternativeDTO(null, "A", "path",true, "",null),
                new AlternativeDTO(null, "B", "path",false,"",null),
                new AlternativeDTO(null, "C", "path",false,"",null),
                new AlternativeDTO(null, "D", "path",false,"",null),
                new AlternativeDTO(null, "E", "path",false,"",null) ))
            .build());

            questionService.createQuestion(QuestionDTO.builder()
            .multipleChoice(true)
            .numberLines(66)
            .educationLevel(QuestionEducationLevel.ENSINO_MÉDIO)
            .personId(11L)
            .headerId(1L)
            .answerId(0L)
            .enable(true)
            .accessLevel(QuestionAccessLevel.PRIVATE)
            .tagIds(Set.of(1L))
            .alternatives(Set.of(
                new AlternativeDTO(null, "A", "path",true, "",null),
                new AlternativeDTO(null, "B", "path",false,"",null),
                new AlternativeDTO(null, "C", "path",false,"",null),
                new AlternativeDTO(null, "D", "path",false,"",null),
                new AlternativeDTO(null, "E", "path",false,"",null) ))
            .build());
    }

}
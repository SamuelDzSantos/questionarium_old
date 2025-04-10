package br.com.questionarium.question_service.init;

import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.questionarium.question_service.dto.AlternativeDTO;
import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.dto.TagDTO;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {


        tagService.createTag(TagDTO.builder()
            .name("TI")
            .description("Tecnologia da Informação")
            .build());

        tagService.createTag(TagDTO.builder()
            .name("PT")
            .description("Português")
            .build());

        tagService.createTag(TagDTO.builder()
            .name("Mat-Financeira")
            .description("Matemática Financeira Aplicada")
            .build());

        tagService.createTag(TagDTO.builder().name("Ciência Social").description("Ciência Social").build());

        questionService.createQuestion(QuestionDTO.builder()
            .multipleChoice(true)
            .numberLines(15)
            .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
            .personId(0L)
            .header("A análise dos dados coletados durante o experimento demonstrou que a variável X tem uma relação direta com a variável Y. De acordo com o estudo, qual seria a hipótese mais plausível sobre o impacto dessa relação no comportamento Z?")
            .answerId(0L)
            .enable(true)
            .accessLevel(QuestionAccessLevel.PRIVATE)
            .tagIds(Set.of(1L, 2L))
            .alternatives(Set.of(
                new AlternativeDTO(null, "A", "path",true, "",null, 1),
                new AlternativeDTO(null, "B", "path",false,"",null, 2),
                new AlternativeDTO(null, "C", "path",false,"",null, 3),
                new AlternativeDTO(null, "D", "path",false,"",null, 4),
                new AlternativeDTO(null, "E", "path",false,"",null, 5) ))
            .build());

            questionService.createQuestion(QuestionDTO.builder()
            .multipleChoice(true)
            .numberLines(66)
            .educationLevel(QuestionEducationLevel.ENSINO_MÉDIO)
            .personId(0L)
            .header("Com base na teoria proposta por Smith (2023), a compreensão dos fatores que influenciam a decisão A envolve considerar múltiplos elementos contextuais. Como a aplicação dessa teoria pode ser expandida para o cenário B?\"")
            .answerId(0L)
            .enable(true)
            .accessLevel(QuestionAccessLevel.PRIVATE)
            .tagIds(Set.of(3L))
            .alternatives(Set.of(
                new AlternativeDTO(null, "A", "path",true, "Explanation",null, 1),
                new AlternativeDTO(null, "B", "path",false,"Explanation",null, 2),
                new AlternativeDTO(null, "C", "path",false,"Explanation",null, 3),
                new AlternativeDTO(null, "D", "path",false,"Explanation",null, 4),
                new AlternativeDTO(null, "E", "path",false,"Explanation",null, 5) ))
            .build());
    }

}
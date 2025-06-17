package br.com.questionarium.question_service.init;

import java.util.List;

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
public class DataLoader implements ApplicationRunner {

        private final QuestionService questionService;
        private final TagService tagService;

        @Override
        public void run(ApplicationArguments args) throws Exception {
                // Tags iniciais
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

                tagService.createTag(TagDTO.builder()
                                .name("Ciência Social")
                                .description("Ciência Social")
                                .build());

                // Questões usuário 0
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(15)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(0L)
                                .header("A análise dos dados coletados durante o experimento demonstrou que a variável X tem uma relação direta com a variável Y. De acordo com o estudo, qual seria a hipótese mais plausível sobre o impacto dessa relação no comportamento Z?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(1L, 2L))
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("A")
                                                                .imagePath("path")
                                                                .isCorrect(true)
                                                                .explanation("")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("B")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("C")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("D")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("E")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                // Repita o mesmo padrão para as demais questões
        }
}

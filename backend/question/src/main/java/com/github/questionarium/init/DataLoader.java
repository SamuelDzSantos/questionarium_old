package com.github.questionarium.init;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.github.questionarium.interfaces.DTOs.AlternativeDTO;
import com.github.questionarium.interfaces.DTOs.QuestionDTO;
import com.github.questionarium.interfaces.DTOs.TagDTO;
import com.github.questionarium.services.QuestionService;
import com.github.questionarium.services.TagService;
import com.github.questionarium.types.QuestionAccessLevel;
import com.github.questionarium.types.QuestionEducationLevel;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

        private final QuestionService questionService;
        private final TagService tagService;

        @Override
        public void run(ApplicationArguments args) throws Exception {
                // Tags iniciais
                tagService.createTag(TagDTO.builder().name("Português").description("Português").build());
                tagService.createTag(TagDTO.builder().name("Gramática normativa").description("Gramática normativa")
                                .build());
                tagService.createTag(TagDTO.builder().name("Interpretação de texto")
                                .description("Interpretação de texto").build());
                tagService.createTag(TagDTO.builder().name("Literatura brasileira").description("Literatura brasileira")
                                .build());
                tagService.createTag(TagDTO.builder().name("Literatura portuguesa").description("Literatura portuguesa")
                                .build());
                tagService.createTag(TagDTO.builder().name("Redação").description("Redação").build());
                tagService.createTag(TagDTO.builder().name("Matemática").description("Matemática").build());
                tagService.createTag(TagDTO.builder().name("Aritmética").description("Aritmética").build());
                tagService.createTag(TagDTO.builder().name("Álgebra").description("Álgebra").build());
                tagService.createTag(
                                TagDTO.builder().name("Equação do 2º grau").description("Equação do 2º grau").build());
                tagService.createTag(TagDTO.builder().name("Geometria plana").description("Geometria plana").build());
                tagService.createTag(
                                TagDTO.builder().name("Geometria espacial").description("Geometria espacial").build());
                tagService.createTag(TagDTO.builder().name("Trigonometria").description("Trigonometria").build());
                tagService.createTag(TagDTO.builder().name("Cálculo diferencial").description("Cálculo diferencial")
                                .build());
                tagService.createTag(TagDTO.builder().name("Cálculo integral").description("Cálculo integral").build());
                tagService.createTag(TagDTO.builder().name("Estatística").description("Estatística").build());
                tagService.createTag(TagDTO.builder().name("Probabilidade").description("Probabilidade").build());
                tagService.createTag(TagDTO.builder().name("Física").description("Física").build());
                tagService.createTag(TagDTO.builder().name("Cinemática").description("Cinemática").build());
                tagService.createTag(TagDTO.builder().name("Dinâmica").description("Dinâmica").build());
                tagService.createTag(TagDTO.builder().name("Mecânica dos fluidos").description("Mecânica dos fluidos")
                                .build());
                tagService.createTag(TagDTO.builder().name("Termodinâmica").description("Termodinâmica").build());
                tagService.createTag(TagDTO.builder().name("Óptica").description("Óptica").build());
                tagService.createTag(TagDTO.builder().name("Eletromagnetismo").description("Eletromagnetismo").build());
                tagService.createTag(TagDTO.builder().name("Primeira Lei de Newton")
                                .description("Primeira Lei de Newton").build());
                tagService.createTag(TagDTO.builder().name("Lei de Ampère").description("Lei de Ampère").build());
                tagService.createTag(TagDTO.builder().name("Química").description("Química").build());
                tagService.createTag(TagDTO.builder().name("Química orgânica").description("Química orgânica").build());
                tagService.createTag(
                                TagDTO.builder().name("Química inorgânica").description("Química inorgânica").build());
                tagService.createTag(
                                TagDTO.builder().name("Química analítica").description("Química analítica").build());
                tagService.createTag(TagDTO.builder().name("Biologia").description("Biologia").build());
                tagService.createTag(TagDTO.builder().name("Biologia celular").description("Biologia celular").build());
                tagService.createTag(TagDTO.builder().name("Genética").description("Genética").build());
                tagService.createTag(TagDTO.builder().name("Ecologia").description("Ecologia").build());
                tagService.createTag(TagDTO.builder().name("Evolução").description("Evolução").build());
                tagService.createTag(TagDTO.builder().name("História").description("História").build());
                tagService.createTag(
                                TagDTO.builder().name("História do Brasil").description("História do Brasil").build());
                tagService.createTag(
                                TagDTO.builder().name("Revolução Francesa").description("Revolução Francesa").build());
                tagService.createTag(TagDTO.builder().name("Idade Média").description("Idade Média").build());
                tagService.createTag(TagDTO.builder().name("História Antiga").description("História Antiga").build());
                tagService.createTag(TagDTO.builder().name("História Moderna").description("História Moderna").build());
                tagService.createTag(TagDTO.builder().name("Geografia").description("Geografia").build());
                tagService.createTag(TagDTO.builder().name("Geografia física").description("Geografia física").build());
                tagService.createTag(TagDTO.builder().name("Geografia humana").description("Geografia humana").build());
                tagService.createTag(TagDTO.builder().name("Filosofia").description("Filosofia").build());
                tagService.createTag(TagDTO.builder().name("Sociologia").description("Sociologia").build());
                tagService.createTag(TagDTO.builder().name("Artes").description("Artes").build());
                tagService.createTag(TagDTO.builder().name("Educação Física").description("Educação Física").build());
                tagService.createTag(TagDTO.builder().name("Inglês").description("Inglês").build());
                tagService.createTag(TagDTO.builder().name("Espanhol").description("Espanhol").build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Exemplo de cabecalho questao 1?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(1L, 10L, 20L))
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA A - CORRETA")
                                                                .imagePath("path")
                                                                .isCorrect(true)
                                                                .explanation("")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA B")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA C")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA D")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA E")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                // Questões usuário 3

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Exemplo de cabecalho questao 1?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(1L, 10L, 20L))
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA A - CORRETA")
                                                                .imagePath("path")
                                                                .isCorrect(true)
                                                                .explanation("")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA B")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA C")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA D")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA E")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Exemplo de cabecalho questao 2?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(2L, 12L, 22L))
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA A")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA B - CORRETA")
                                                                .imagePath("path")
                                                                .isCorrect(true)
                                                                .explanation("")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA C")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA D")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA E")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Exemplo de cabecalho questao 3?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(3L, 13L, 23L))
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA A")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA B")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA C - CORRETA")
                                                                .imagePath("path")
                                                                .isCorrect(true)
                                                                .explanation("")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA D")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA E")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Exemplo de cabecalho questao 4?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(4L, 14L, 24L))
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA A")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA B")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA C")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA D - CORRETA")
                                                                .imagePath("path")
                                                                .isCorrect(true)
                                                                .explanation("")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("EXEMPLO DO TEXTO ALTERNATIVA E")
                                                                .imagePath("path")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

        }
}

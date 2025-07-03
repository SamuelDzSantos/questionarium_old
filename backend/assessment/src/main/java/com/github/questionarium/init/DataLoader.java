package com.github.questionarium.init;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.github.questionarium.model.AssessmentHeader;
import com.github.questionarium.model.AssessmentModel;
import com.github.questionarium.model.QuestionWeight;
import com.github.questionarium.service.AppliedAssessmentService;
import com.github.questionarium.service.AssessmentHeaderService;
import com.github.questionarium.service.AssessmentModelService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final AssessmentHeaderService assessmentHeaderService;
    private final AssessmentModelService assessmentModelService;
    private final AppliedAssessmentService appliedAssessmentService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // USER1 - Colégio Estadual do Paraná - Português
        assessmentHeaderService.createHeader(
                AssessmentHeader.builder()
                        .institution("Colégio Estadual do Paraná")
                        .department("Departamento de Linguagens")
                        .course("Português")
                        .classroom("Turma da Manhã")
                        .professor("User 1")
                        .instructions("Responda a todas as questões com atenção. Boa prova!")
                        .image(null)
                        .creationDateTime(LocalDateTime.now())
                        .updateDateTime(LocalDateTime.now())
                        .build(),
                3L);

        assessmentHeaderService.createHeader(
                AssessmentHeader.builder()
                        .institution("Colégio Estadual do Paraná")
                        .department("Departamento de Linguagens")
                        .course("Português")
                        .classroom("Turma da Tarde")
                        .professor("User 1")
                        .instructions("Leia atentamente cada enunciado e marque a alternativa correta.")
                        .image(null)
                        .creationDateTime(LocalDateTime.now())
                        .updateDateTime(LocalDateTime.now())
                        .build(),
                3L);

        // JOAO - IFPR - História
        assessmentHeaderService.createHeader(
                AssessmentHeader.builder()
                        .institution("IFPR")
                        .department("Departamento de História")
                        .course("História")
                        .classroom("Petróleo")
                        .professor("João")
                        .instructions("Prova destinada aos alunos do curso de Petróleo. Responda com clareza.")
                        .image(null)
                        .creationDateTime(LocalDateTime.now())
                        .updateDateTime(LocalDateTime.now())
                        .build(),
                5L);

        assessmentHeaderService.createHeader(
                AssessmentHeader.builder()
                        .institution("IFPR")
                        .department("Departamento de História")
                        .course("História")
                        .classroom("Secretariado")
                        .professor("João")
                        .instructions(
                                "Avaliação para turma de Secretariado. Todas as respostas devem ser justificadas.")
                        .image(null)
                        .creationDateTime(LocalDateTime.now())
                        .updateDateTime(LocalDateTime.now())
                        .build(),
                5L);

        assessmentHeaderService.createHeader(
                AssessmentHeader.builder()
                        .institution("IFPR")
                        .department("Departamento de História")
                        .course("História")
                        .classroom("Redes")
                        .professor("João")
                        .instructions("Responda às questões baseando-se nas aulas e textos indicados.")
                        .image(null)
                        .creationDateTime(LocalDateTime.now())
                        .updateDateTime(LocalDateTime.now())
                        .build(),
                5L);

        AssessmentModel modelo = new AssessmentModel();
        modelo.setDescription("Modelo misto: 3 questões do João e 2 da UFPR de História.");
        List<QuestionWeight> questoes = new ArrayList<>();
        questoes.add(new QuestionWeight(18L, 1.0, 1));
        questoes.add(new QuestionWeight(17L, 1.0, 2));
        questoes.add(new QuestionWeight(16L, 1.0, 3));
        questoes.add(new QuestionWeight(8L, 1.0, 4));
        questoes.add(new QuestionWeight(7L, 1.0, 5));
        modelo.setQuestions(questoes);
        modelo.setCreationDateTime(LocalDateTime.now());
        modelo.setUpdateDateTime(LocalDateTime.now());
        modelo.setUserId(5L);
        modelo.setInstitution("IFPR");
        modelo.setDepartment("Departamento de História");
        modelo.setCourse("História");
        modelo.setClassroom("Petroleo");
        modelo.setProfessor("João");
        modelo.setInstructions("Responda com atenção.");
        modelo.setImage(null);
        assessmentModelService.createAssessment(modelo, 5L);

        AssessmentModel modelo2 = new AssessmentModel();
        modelo2.setDescription("Modelo de avaliação autoral do João");
        List<QuestionWeight> questoes2 = new ArrayList<>();
        questoes2.add(new QuestionWeight(14L, 0.5, 1));
        questoes2.add(new QuestionWeight(15L, 0.5, 2));
        questoes2.add(new QuestionWeight(16L, 0.5, 3));
        questoes2.add(new QuestionWeight(17L, 0.5, 4));
        questoes2.add(new QuestionWeight(18L, 0.5, 5));
        modelo2.setQuestions(questoes2);
        modelo2.setCreationDateTime(LocalDateTime.now());
        modelo2.setUpdateDateTime(LocalDateTime.now());
        modelo2.setUserId(5L);
        modelo2.setInstitution("IFPR");
        modelo2.setDepartment("Departamento de História");
        modelo2.setCourse("História");
        modelo2.setClassroom("Secretariado");
        modelo2.setProfessor("João");
        modelo2.setInstructions("Peso 0.5 cada. Boa prova!");
        modelo2.setImage(null);
        assessmentModelService.createAssessment(modelo2, 5L);

        // Aplicação 1: Turma da Manhã de Petróleo (sem embaralhamento)
        appliedAssessmentService.applyAssessment(
                2L, // id do modelo
                "Turma de Petróleo - 02/2025",
                LocalDate.of(2025, 8, 1),
                10, // quantity
                false, // shuffleQuestions
                5L, // userId (João)
                false // isAdmin, se rodando como admin no DataLoader
        );

        // Aplicação 2: Turma de Secretariado (com embaralhamento)
        appliedAssessmentService.applyAssessment(
                2L, // id do modelo
                "Turma de Secretariado - 02/2025",
                LocalDate.of(2025, 8, 1),
                5, // quantity
                true, // shuffleQuestions
                5L, // userId (João)
                false // isAdmin
        );

    }
}

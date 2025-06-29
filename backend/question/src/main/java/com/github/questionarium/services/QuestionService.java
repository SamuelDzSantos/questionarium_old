package com.github.questionarium.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.questionarium.interfaces.DTOs.AlternativeDTO;
import com.github.questionarium.interfaces.DTOs.AnswerKeyDTO;
import com.github.questionarium.interfaces.DTOs.QuestionDTO;
import com.github.questionarium.model.Alternative;
import com.github.questionarium.model.Question;
import com.github.questionarium.model.Tag;
import com.github.questionarium.repository.AlternativeRepository;
import com.github.questionarium.repository.QuestionRepository;
import com.github.questionarium.repository.TagRepository;
import com.github.questionarium.services.mappers.QuestionMapper;
import com.github.questionarium.types.QuestionAccessLevel;
import com.github.questionarium.types.QuestionEducationLevel;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final AlternativeRepository alternativeRepository;
    private final QuestionMapper questionMapper;
    private final TagRepository tagRepository;

    public QuestionService(QuestionRepository questionRepository,
            AlternativeRepository alternativeRepository,
            QuestionMapper questionMapper, TagRepository tagRepository) {
        this.questionRepository = questionRepository;
        this.alternativeRepository = alternativeRepository;
        this.questionMapper = questionMapper;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        logger.info("Criando nova questão para usuário {}", questionDTO.getUserId());

        var correctAlternatives = questionDTO.getAlternatives().stream()
                .filter(AlternativeDTO::getIsCorrect)
                .collect(Collectors.toList());
        if (correctAlternatives.isEmpty() && questionDTO.getMultipleChoice()) {
            throw new IllegalArgumentException("Nenhuma alternativa correta fornecida.");
        }
        if (!Boolean.TRUE.equals(questionDTO.getMultipleChoice()) && correctAlternatives.size() > 1) {
            throw new IllegalArgumentException(
                    "Mais de uma alternativa correta fornecida para questão não múltipla escolha.");
        }

        // Mapear DTO para entidade
        Question question = questionMapper.toEntity(questionDTO);

        // Preencher alternativas
        Set<Alternative> alternatives = questionDTO.getAlternatives().stream()
                .map(dto -> Alternative.builder()
                        .description(dto.getDescription())
                        .imagePath(dto.getImagePath())
                        .explanation(dto.getExplanation())
                        .isCorrect(dto.getIsCorrect())
                        .alternativeOrder(dto.getAlternativeOrder())
                        .question(question)
                        .build())
                .collect(Collectors.toSet());
        question.setAlternatives(alternatives);

        // Preenche Tags
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(questionDTO.getTagIds()));
        question.setTags(tags);

        // Salvar para gerar IDs
        Question saved = questionRepository.save(question);

        if (questionDTO.getMultipleChoice()) {
            // Determinar gabarito
            Alternative correct = saved.getAlternatives().stream()
                    .filter(Alternative::getIsCorrect)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Nenhuma alternativa correta encontrada."));
            saved.setAnswerId(correct.getId());
        }

        // Re-salvar com gabarito
        Question updated = questionRepository.save(saved);
        return questionMapper.toDto(updated);
    }

    public List<QuestionDTO> getFilteredQuestions(
            Long userId,
            Boolean isAdmin,
            Boolean multipleChoice,
            List<Long> tagIds,
            Integer accessLevel,
            Integer educationLevel,
            String header) {
        Specification<Question> spec;

        log.info("Buscando questões para user {}", userId);

        // Se admin cria uma condição where vazia.
        if (isAdmin) {
            spec = (root, query, cb) -> null;
        }
        // Caso contrário retorna apenas linhas ativas que sejam do usuário ou publicas
        else {
            QuestionAccessLevel acl = QuestionAccessLevel.PUBLIC;

            spec = (root, query, cb) -> {

                // Pega linhas do usuário ativas
                Predicate userCondition = cb.and(
                        cb.equal(root.get("userId"), userId),
                        cb.isTrue(root.get("enable")));

                // Pega linhas publicas ativas
                Predicate publicCondition = cb.and(
                        cb.equal(root.get("accessLevel"), acl),
                        cb.isTrue(root.get("enable")));
                // Une as duas condições por meio de um or
                return cb.or(userCondition, publicCondition);
            };
        }

        // Segue com especificações gerais

        if (multipleChoice != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("multipleChoice"), multipleChoice));
        }
        if (header != null) {
            spec = spec
                    .and((root, query, cb) -> cb.like(cb.lower(root.get("header")), "%" + header.toLowerCase() + "%"));
        }
        if (tagIds != null && !tagIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                var join = root.join("tags");
                return join.get("id").in(tagIds);
            });
        }
        if (accessLevel != null) {
            QuestionAccessLevel acl = QuestionAccessLevel.values()[accessLevel];
            spec = spec.and((root, query, cb) -> cb.equal(root.get("accessLevel"), acl));
        }
        if (educationLevel != null) {
            QuestionEducationLevel ed = QuestionEducationLevel.values()[educationLevel];
            spec = spec.and((root, query, cb) -> cb.equal(root.get("educationLevel"), ed));
        }
        System.out.println("--------------------------------------------------------");
        // return questionRepository.findAll(spec).stream()
        return questionRepository.findAll().stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<QuestionDTO> getQuestionById(Long id, Long userId, Boolean isAdmin) {

        if (isAdmin)
            return questionRepository.findById(id)
                    .map(questionMapper::toDto);

        return questionRepository.findById(id)
                .filter(q -> q.isEnable())
                .filter(q -> q.getUserId().equals(userId)
                        || QuestionAccessLevel.PUBLIC.equals(q.getAccessLevel()))
                .map(questionMapper::toDto);
    }

    @Transactional
    public List<AnswerKeyDTO> getAnswerKeys(List<Long> questionIds, Long userId) {
        List<AnswerKeyDTO> keys = new ArrayList<>();
        for (Long id : questionIds) {
            questionRepository.findById(id).ifPresent(q -> {
                if (q.getUserId().equals(userId) ||
                        QuestionAccessLevel.PUBLIC.equals(q.getAccessLevel())) {
                    keys.add(new AnswerKeyDTO(q.getId(), q.getAnswerId()));
                }
            });
        }
        return keys;
    }

    @Transactional
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO, Long userId, Boolean isAdmin) {

        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questão não encontrada: " + id));

        // Caso não seja admin nem seja dono da question rejeita update.
        if (!isAdmin && !q.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Permissão negada.");
        }

        if (!isAdmin) {
            questionDTO.setUserId(userId);
        }

        // Atualiza campos básicos...
        q.setMultipleChoice(questionDTO.getMultipleChoice());
        q.setNumberLines(questionDTO.getNumberLines());
        q.setHeader(questionDTO.getHeader());
        q.setHeaderImage(questionDTO.getHeaderImage());
        q.setEnable(questionDTO.getEnable());
        q.setAccessLevel(questionDTO.getAccessLevel());
        q.setEducationLevel(questionDTO.getEducationLevel());

        // Limpa e adiciona novas alternativas (sem answerId ainda)
        q.getAlternatives().clear();
        Set<Alternative> novos = questionDTO.getAlternatives().stream()
                .map(dto -> Alternative.builder()
                        .description(dto.getDescription())
                        .imagePath(dto.getImagePath())
                        .explanation(dto.getExplanation())
                        .isCorrect(dto.getIsCorrect())
                        .alternativeOrder(dto.getAlternativeOrder())
                        .question(q)
                        .build())
                .collect(Collectors.toSet());
        q.getAlternatives().addAll(novos);

        // 1) Persiste para gerar IDs nas alternativas
        Question saved = questionRepository.save(q);

        // 2) Encontra a alternativa correta já com ID
        Alternative corret = saved.getAlternatives().stream()
                .filter(Alternative::getIsCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma alternativa correta fornecida."));

        // 3) Atualiza o answerId no question e salva de novo
        saved.setAnswerId(corret.getId());
        Question updated = questionRepository.save(saved);

        return questionMapper.toDto(updated);
    }

    @Transactional
    public void deleteQuestion(Long id, Long userId, Boolean isAdmin) {

        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questão não encontrada: " + id));

        // Se não for admin e não for dono da query rejeita delete
        if (!isAdmin && !q.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Permissão negada.");
        }
        q.setEnable(false);
        questionRepository.save(q);
    }

    // Métodos usados pelo Rabbit consumer

    @Transactional(readOnly = true)
    public QuestionDTO getQuestionAsDto(Long id) {
        return questionRepository.findById(id)
                .map(questionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Questão não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<AlternativeDTO> getAlternativesAsDto(Long questionId) {
        return alternativeRepository.findAllByQuestionId(questionId)
                .stream()
                .map(a -> new AlternativeDTO(
                        a.getId(),
                        a.getDescription(),
                        a.getImagePath(),
                        a.getIsCorrect(),
                        a.getExplanation(),
                        a.getAlternativeOrder()))
                .collect(Collectors.toList());
    }
}

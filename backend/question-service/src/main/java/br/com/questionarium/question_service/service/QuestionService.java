package br.com.questionarium.question_service.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.questionarium.question_service.dto.AlternativeDTO;
import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.dto.QuestionMapper;
import br.com.questionarium.question_service.model.Alternative;
import br.com.questionarium.question_service.model.Question;
import br.com.questionarium.question_service.model.Tag;
import br.com.questionarium.question_service.repository.AlternativeRepository;
import br.com.questionarium.question_service.repository.QuestionRepository;
import br.com.questionarium.question_service.repository.TagRepository;
import br.com.questionarium.question_service.types.QuestionAccessLevel;
import br.com.questionarium.question_service.types.QuestionEducationLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Join;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final AlternativeRepository alternativeRepository;
    private QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);

    public QuestionService(QuestionRepository questionRepository,
            TagRepository tagRepository,
            AlternativeRepository alternativeRepository,
            QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
        this.alternativeRepository = alternativeRepository;
        this.questionMapper = questionMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        logger.info("Criando nova questão para usuário {}", questionDTO.getUserId());

        // Validar alternativas corretas
        List<AlternativeDTO> correctAlternatives = questionDTO.getAlternatives().stream()
                .filter(AlternativeDTO::getIsCorrect)
                .toList();
        if (correctAlternatives.isEmpty()) {
            logger.error("Nenhuma alternativa correta fornecida");
            throw new IllegalArgumentException("No correct alternative provided.");
        }
        if (correctAlternatives.size() > 1) {
            logger.error("Mais de uma alternativa correta fornecida");
            throw new IllegalArgumentException("More than one correct alternative provided.");
        }

        // Mapear DTO para entidade
        Question question = questionMapper.toEntity(questionDTO);

        // Associar tags
        QuestionServiceHelper.setTags(questionDTO, question, tagRepository);

        // Vincular cada alternativa à questão
        question.getAlternatives().forEach(alt -> alt.setQuestion(question));

        // Salvar questão SEM o answerId (temporariamente null)
        question.setAnswerId(null); // Garantido
        Question savedQuestion = questionRepository.save(question);

        // AGORA, obter o ID correto
        Alternative correctAlternative = savedQuestion.getAlternatives().stream()
                .filter(Alternative::getIsCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No correct alternative provided"));

        // Atualizar answerId e re-salvar
        savedQuestion.setAnswerId(correctAlternative.getId());
        savedQuestion = questionRepository.save(savedQuestion);

        QuestionDTO resultDTO = questionMapper.toDTO(savedQuestion);
        logger.info("Questão criada com sucesso: {}", resultDTO);
        return resultDTO;
    }

    public List<QuestionDTO> getAllQuestions() {
        logger.info("Buscando todas as questões");
        List<Question> questions = questionRepository.findAll();
        List<QuestionDTO> result = questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Foram encontradas {} questões", result.size());
        return result;
    }

    public List<QuestionDTO> getFilteredQuestions(
            Long userId,
            Boolean multipleChoice,
            List<Long> tagIds,
            Integer accessLevel,
            Integer educationLevel,
            String header) {
        logger.info(
                "Buscando questões filtradas: userId={}, multipleChoice={}, tagIds={}, accessLevel={}, educationLevel={}, header={}",
                userId, multipleChoice, tagIds, accessLevel, educationLevel, header);

        Specification<Question> spec = Specification.where((root, query, cb) -> cb.equal(root.get("enable"), true));

        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userId"), userId));
        }
        if (multipleChoice != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("multipleChoice"), multipleChoice));
        }
        if (header != null) {
            spec = spec
                    .and((root, query, cb) -> cb.like(cb.lower(root.get("header")), "%" + header.toLowerCase() + "%"));
        }
        if (tagIds != null && !tagIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<Question, Tag> tagJoin = root.join("tags");
                return tagJoin.get("id").in(tagIds);
            });
        }
        if (accessLevel != null) {
            QuestionAccessLevel accessLevelEnum = QuestionAccessLevel.values()[accessLevel];
            spec = spec.and((root, query, cb) -> cb.equal(root.get("accessLevel"), accessLevelEnum));
        }
        if (educationLevel != null) {
            QuestionEducationLevel educationLevelEnum = QuestionEducationLevel.values()[educationLevel];
            spec = spec.and((root, query, cb) -> cb.equal(root.get("educationLevel"), educationLevelEnum));
        }

        List<Question> questions = questionRepository.findAll(spec);
        List<QuestionDTO> result = questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());

        logger.info("Foram encontradas {} questões após filtragem", result.size());
        return result;
    }

    public Optional<QuestionDTO> getQuestionById(Long id) {
        logger.info("Buscando questão por ID {}", id);
        Optional<QuestionDTO> result = questionRepository.findById(id)
                .map(questionMapper::toDTO);
        if (result.isPresent()) {
            logger.info("Questão {} encontrada", id);
        } else {
            logger.warn("Questão {} não encontrada", id);
        }
        return result;
    }

    @Transactional
    public List<AnswerKeyDTO> getAnswerKeys(List<Long> questionsIds) {
        logger.info("Buscando answer keys para questões {}", questionsIds);
        List<AnswerKeyDTO> pairs = new ArrayList<>();
        for (Long id : questionsIds) {
            Optional<QuestionDTO> dto = questionRepository.findById(id)
                    .map(questionMapper::toDTO);
            if (dto.isPresent()) {
                pairs.add(new AnswerKeyDTO(dto.get().getId(), dto.get().getAnswerId()));
            } else {
                logger.warn("Questão {} não encontrada ao buscar answer key", id);
            }
        }
        logger.info("Answer keys retornados: {}", pairs);
        return pairs;
    }

    @Transactional
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        logger.info("Atualizando questão {} com dados {}", id, questionDTO);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Questão {} não encontrada para atualizar", id);
                    return new EntityNotFoundException("Question not found with ID " + id);
                });

        question.setMultipleChoice(questionDTO.isMultipleChoice());
        question.setNumberLines(questionDTO.getNumberLines());
        question.setUserId(questionDTO.getUserId());
        question.setHeader(questionDTO.getHeader());
        question.setHeaderImage(questionDTO.getHeaderImage());
        question.setEnable(questionDTO.isEnable());
        question.setAccessLevel(questionDTO.getAccessLevel());
        question.setEducationLevel(questionDTO.getEducationLevel());

        QuestionServiceHelper.setTags(questionDTO, question, tagRepository);

        // Processar alternativas existentes e novas
        Set<Alternative> updatedAlternatives = new HashSet<>();
        List<AlternativeDTO> correctAlternatives = questionDTO.getAlternatives().stream()
                .filter(AlternativeDTO::getIsCorrect)
                .collect(Collectors.toList());

        if (correctAlternatives.isEmpty()) {
            logger.error("Nenhuma alternativa correta fornecida ao atualizar questão {}", id);
            throw new IllegalArgumentException("No correct alternative provided.");
        }
        if (correctAlternatives.size() > 1) {
            logger.error("Mais de uma alternativa correta fornecida ao atualizar questão {}", id);
            throw new IllegalArgumentException("More than one correct alternative provided.");
        }

        for (AlternativeDTO alternativeDTO : questionDTO.getAlternatives()) {
            if (alternativeDTO.getId() != null) {
                Alternative existingAlternative = alternativeRepository.findById(alternativeDTO.getId())
                        .orElseThrow(() -> {
                            logger.error("Alternative {} não encontrada ao atualizar questão {}",
                                    alternativeDTO.getId(), id);
                            return new IllegalArgumentException("Alternative not found");
                        });
                existingAlternative.setDescription(alternativeDTO.getDescription());
                existingAlternative.setExplanation(alternativeDTO.getExplanation());
                existingAlternative.setImagePath(alternativeDTO.getImagePath());
                existingAlternative.setIsCorrect(alternativeDTO.getIsCorrect());
                updatedAlternatives.add(alternativeRepository.save(existingAlternative));
                logger.info("Alternative {} atualizada para questão {}", existingAlternative.getId(), id);
            } else {
                Alternative newAlternative = Alternative.builder()
                        .description(alternativeDTO.getDescription())
                        .imagePath(alternativeDTO.getImagePath())
                        .isCorrect(alternativeDTO.getIsCorrect())
                        .question(question)
                        .build();
                Alternative savedAlt = alternativeRepository.save(newAlternative);
                updatedAlternatives.add(savedAlt);
                logger.info("Nova alternative {} criada para questão {}", savedAlt.getId(), id);
            }
        }

        // Definir answerId se houver alternativa correta
        Alternative correctAlternativeEntity = updatedAlternatives.stream()
                .filter(Alternative::getIsCorrect)
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Nenhuma alternativa correta encontrada após atualizar questão {}", id);
                    return new IllegalArgumentException("No correct alternative provided.");
                });
        question.setAnswerId(correctAlternativeEntity.getId());
        questionRepository.save(question);
        logger.info("AnswerId {} definido para questão {}", correctAlternativeEntity.getId(), id);

        questionRepository.flush();
        entityManager.refresh(question);

        Question updatedQuestion = questionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Questão {} não encontrada após atualização", id);
                    return new EntityNotFoundException("Question not found with ID " + id);
                });
        QuestionDTO resultDTO = questionMapper.toDTO(updatedQuestion);
        logger.info("Questão {} atualizada com sucesso: {}", id, resultDTO);
        return resultDTO;
    }

    public void deleteQuestion(Long id) {
        logger.info("Desativando (soft delete) questão {}", id);
        questionRepository.findById(id)
                .map(question -> {
                    question.setEnable(false);
                    Question updatedQuestion = questionRepository.save(question);
                    logger.info("Questão {} marcada como inativa", updatedQuestion.getId());
                    return updatedQuestion;
                })
                .orElseThrow(() -> {
                    logger.error("Questão {} não encontrada para exclusão", id);
                    return new RuntimeException("Question not found with ID " + id);
                });
    }
}

class QuestionServiceHelper {

    public static void setTags(QuestionDTO questionDTO, Question question, TagRepository tagRepository) {
        if (questionDTO.getTagIds() != null && !questionDTO.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : questionDTO.getTagIds()) {
                Optional<Tag> tagOpt = tagRepository.findById(tagId);
                if (tagOpt.isPresent()) {
                    tags.add(tagOpt.get());
                }
            }
            question.setTags(tags);
        }
    }
}

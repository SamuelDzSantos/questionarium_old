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
            logger.error("Nenhuma alternativa correta fornecida ao atualizar questão {}.");
            throw new IllegalArgumentException("Nenhuma alternativa correta fornecida.");
        }

        if (!questionDTO.isMultipleChoice() && correctAlternatives.size() > 1) {
            logger.error(
                    "Mais de uma alternativa correta fornecida ao atualizar questão {} (questão NÃO é de múltipla escolha).");
            throw new IllegalArgumentException(
                    "Mais de uma alternativa correta fornecida para questão não múltipla escolha.");
        }
        // Mapear DTO para entidade
        Question question = questionMapper.toEntity(questionDTO);

        // Associar tags
        QuestionServiceHelper.setTags(questionDTO, question, tagRepository);

        // Salvar questão SEM o answerId (temporariamente null)
        question.setAnswerId(null); // Garantido
        Question savedQuestion = questionRepository.save(question);

        // AGORA, obter o ID correto
        Alternative correctAlternative = savedQuestion.getAlternatives().stream()
                .filter(Alternative::getIsCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma alternativa correta fornecida."));

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

        logger.info("Foram encontradas {} questões após filtragem.", result.size());
        return result;
    }

    public List<QuestionDTO> getFilteredQuestionsAsAdmin(
            Long userId,
            Boolean multipleChoice,
            List<Long> tagIds,
            Integer accessLevel,
            Integer educationLevel,
            String header) {

        logger.info(
                "Buscando questões filtradas (ADMIN): userId={}, multipleChoice={}, tagIds={}, accessLevel={}, educationLevel={}, header={}",
                userId, multipleChoice, tagIds, accessLevel, educationLevel, header);

        Specification<Question> spec = Specification.where(null); // ADMIN → não filtra enable

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

        logger.info("Foram encontradas {} questões após filtragem (ADMIN).", result.size());
        return result;
    }

    public Optional<QuestionDTO> getQuestionById(Long id) {
        logger.info("Buscando questão por ID {}.", id);
        Optional<QuestionDTO> result = questionRepository.findById(id)
                .map(questionMapper::toDTO);
        if (result.isPresent()) {
            logger.info("Questão {} encontrada.", id);
        } else {
            logger.warn("Questão {} não encontrada.", id);
        }
        return result;
    }

    public Optional<QuestionDTO> getQuestionByIdForUser(Long id, Long userId) {
        logger.info("Buscando questão por ID {} para userId={}.", id, userId);

        return questionRepository.findById(id)
                .filter(question -> question.isEnable())
                .filter(question -> question.getUserId().equals(userId)
                        || QuestionAccessLevel.PUBLIC.equals(question.getAccessLevel()))
                .map(questionMapper::toDTO)
                .map(dto -> {
                    logger.info("Questão {} acessível para userId={}, retornando.", id, userId);
                    return dto;
                })
                .or(() -> {
                    logger.warn("Questão {} não acessível para userId={}. Não pertence ao usuário ou não é PÚBLICA.",
                            id,
                            userId);
                    return Optional.empty();
                });
    }

    @Transactional
    public List<AnswerKeyDTO> getAnswerKeys(List<Long> questionsIds, Long userId) {
        logger.info("Buscando gabaritos para questões {} para userId={}.", questionsIds, userId);

        List<AnswerKeyDTO> pairs = new ArrayList<>();

        for (Long id : questionsIds) {
            questionRepository.findById(id).ifPresentOrElse(question -> {
                boolean isOwner = question.getUserId().equals(userId);
                boolean isPublic = QuestionAccessLevel.PUBLIC.equals(question.getAccessLevel());

                if (isOwner || isPublic) {
                    pairs.add(new AnswerKeyDTO(question.getId(), question.getAnswerId()));
                    logger.info("Gabarito adicionada para questão {} (userId={}, PUBLIC={})", id, isOwner, isPublic);
                } else {
                    logger.warn("Usuário {} não tem permissão para acessar gabarito da questão {}.", userId, id);
                }
            }, () -> {
                logger.warn("Questão {} não encontrada ao buscar gabarito.", id);
            });
        }

        logger.info("Gabarito retornado: {}.", pairs);
        return pairs;
    }

    @Transactional
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO, Long userId) {
        logger.info("Atualizando questão {} com dados {} para userId={}.", id, questionDTO, userId);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Questão {} não encontrada para atualizar.", id);
                    return new EntityNotFoundException("Pergunta não encontrada com ID " + id);
                });

        // Validação de segurança
        if (!question.getUserId().equals(userId)) {
            logger.warn("Usuário {} não tem permissão para atualizar questão {}.", userId, id);
            throw new EntityNotFoundException("Você não tem permissão para atualizar esta pergunta.");
        }

        // Atualizar campos da questão
        question.setMultipleChoice(questionDTO.isMultipleChoice());
        question.setNumberLines(questionDTO.getNumberLines());
        question.setHeader(questionDTO.getHeader());
        question.setHeaderImage(questionDTO.getHeaderImage());
        question.setEnable(questionDTO.isEnable());
        question.setAccessLevel(questionDTO.getAccessLevel());
        question.setEducationLevel(questionDTO.getEducationLevel());

        QuestionServiceHelper.setTags(questionDTO, question, tagRepository);

        // Limpar alternativas antigas (orphanRemoval cuida da exclusão)
        question.getAlternatives().clear();

        // Criar novas alternativas e vincular
        Set<Alternative> novasAlternativas = questionDTO.getAlternatives().stream()
                .map(altDTO -> Alternative.builder()
                        .description(altDTO.getDescription())
                        .imagePath(altDTO.getImagePath())
                        .explanation(altDTO.getExplanation())
                        .isCorrect(altDTO.getIsCorrect())
                        .alternativeOrder(altDTO.getAlternativeOrder())
                        .build())
                .collect(Collectors.toSet());

        question.getAlternatives().addAll(novasAlternativas);

        // Validar alternativas corretas
        List<Alternative> corretas = novasAlternativas.stream()
                .filter(Alternative::getIsCorrect)
                .toList();

        if (corretas.isEmpty()) {
            logger.error("Nenhuma alternativa correta fornecida ao atualizar questão {}.", id);
            throw new IllegalArgumentException("Nenhuma alternativa correta fornecida.");
        }

        if (!question.isMultipleChoice() && corretas.size() > 1) {
            logger.error("Mais de uma alternativa correta fornecida para questão NÃO múltipla {}.", id);
            throw new IllegalArgumentException("Mais de uma alternativa correta fornecida.");
        }

        question.setAnswerId(corretas.get(0).getId());

        // Salvar e sincronizar
        Question updated = questionRepository.save(question);
        questionRepository.flush();
        entityManager.refresh(updated);

        QuestionDTO resultDTO = questionMapper.toDTO(updated);
        logger.info("Questão {} atualizada com sucesso: {}.", id, resultDTO);
        return resultDTO;
    }

    @Transactional
    public QuestionDTO updateQuestionAsAdmin(Long id, QuestionDTO questionDTO) {
        logger.info("Atualizando questão {} como ADMIN.", id);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Questão {} não encontrada para atualizar (ADMIN).", id);
                    return new EntityNotFoundException("Questao não encontrada com ID." + id);
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
            logger.error("Nenhuma alternativa correta fornecida ao atualizar questão {}.", id);
            throw new IllegalArgumentException("Nenhuma alternativa correta fornecida.");
        }
        if (correctAlternatives.size() > 1) {
            logger.error("Mais de uma alternativa correta fornecida ao atualizar questão {}.", id);
            throw new IllegalArgumentException("Mais de uma alternativa correta fornecida.");
        }

        for (AlternativeDTO alternativeDTO : questionDTO.getAlternatives()) {
            if (alternativeDTO.getId() != null) {
                Alternative existingAlternative = alternativeRepository.findById(alternativeDTO.getId())
                        .orElseThrow(() -> {
                            logger.error("Alternative {} não encontrada ao atualizar questão {}.",
                                    alternativeDTO.getId(), id);
                            return new IllegalArgumentException("Alternativa não encontrada.");
                        });
                existingAlternative.setDescription(alternativeDTO.getDescription());
                existingAlternative.setExplanation(alternativeDTO.getExplanation());
                existingAlternative.setImagePath(alternativeDTO.getImagePath());
                existingAlternative.setIsCorrect(alternativeDTO.getIsCorrect());
                updatedAlternatives.add(alternativeRepository.save(existingAlternative));
                logger.info("Alternativa {} atualizada para questão {}.", existingAlternative.getId(), id);
            } else {
                Alternative newAlternative = Alternative.builder()
                        .description(alternativeDTO.getDescription())
                        .imagePath(alternativeDTO.getImagePath())
                        .isCorrect(alternativeDTO.getIsCorrect())
                        .build();
                Alternative savedAlt = alternativeRepository.save(newAlternative);
                updatedAlternatives.add(savedAlt);
                logger.info("Nova alternative {} criada para questão {}.", savedAlt.getId(), id);
            }
        }

        // Definir answerId se houver alternativa correta
        Alternative correctAlternativeEntity = updatedAlternatives.stream()
                .filter(Alternative::getIsCorrect)
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Nenhuma alternativa correta encontrada após atualizar questão {}.", id);
                    return new IllegalArgumentException("Nenhuma alternativa correta fornecida.");
                });
        question.setAnswerId(correctAlternativeEntity.getId());
        questionRepository.save(question);
        logger.info("Id de resposta {} definido para questão {}.", correctAlternativeEntity.getId(), id);

        questionRepository.flush();
        entityManager.refresh(question);

        Question updatedQuestion = questionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Questão {} não encontrada após atualização.", id);
                    return new EntityNotFoundException("Questão não encontrada com ID." + id);
                });
        QuestionDTO resultDTO = questionMapper.toDTO(updatedQuestion);
        logger.info("Questão {} atualizada com sucesso: {}", id, resultDTO);

        return resultDTO;

    }

    @Transactional
    public void deleteQuestion(Long id, Long userId) {
        logger.info("Tentando deletar (soft delete) questão {} para userId={}", id, userId);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Questão {} não encontrada para exclusão", id);
                    throw new EntityNotFoundException("Questão não encontrada com o ID " + id);
                });

        if (!question.getUserId().equals(userId)) {
            logger.warn("Usuário {} não tem permissão para deletar a questão {}", userId, id);
            throw new EntityNotFoundException("Você não tem permissão para deletar esta questão.");
        }

        question.setEnable(false);
        questionRepository.save(question);

        logger.info("Questão {} foi desativada com sucesso pelo usuário {}", id, userId);
    }

    @Transactional
    public void deleteQuestionAsAdmin(Long id) {
        logger.info("Tentando deletar (soft delete) questão {} como ADMIN", id);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Questão {} não encontrada para exclusão (ADMIN)", id);
                    throw new EntityNotFoundException("Questão não encontrada com o ID " + id);
                });

        question.setEnable(false);
        questionRepository.save(question);

        logger.info("Questão {} foi desativada com sucesso (ADMIN)", id);
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

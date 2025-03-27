package br.com.questionarium.question_service.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final AlternativeRepository alternativeRepository;
    private final QuestionMapper questionMapper;

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

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {

        List<AlternativeDTO> correctAlternatives = questionDTO.getAlternatives().stream()
                .filter(AlternativeDTO::getIsCorrect)
                .toList();

        if (correctAlternatives.isEmpty()) {
            throw new IllegalArgumentException("No correct alternative provided.");
        }

        if (correctAlternatives.size() > 1) {
            throw new IllegalArgumentException("More than one correct alternative provided.");
        }

        Question question = questionMapper.toEntity(questionDTO);

        QuestionServiceHelper.setTags(questionDTO, question, tagRepository);

        question.getAlternatives().forEach(alternative -> alternative.setQuestion(question));

        Question savedQuestion = questionRepository.save(question);

        Alternative correctAlternative = savedQuestion.getAlternatives().stream()
                .filter(Alternative::getIsCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No correct alternative provided"));

        savedQuestion.setAnswerId(correctAlternative.getId());

        savedQuestion = questionRepository.save(savedQuestion);

        return questionMapper.toDTO(savedQuestion);
    }

    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getFilteredQuestions(
            Long personId,
            Boolean multipleChoice,
            List<Long> tagIds,
            Integer accessLevel,
            Integer educationLevel,
            String header
            ) {

        Specification<Question> spec = Specification.where((root, query, cb) -> cb.equal(root.get("enable"), true));

        if (personId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("personId"), personId));
        }

        if (multipleChoice != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("multipleChoice"), multipleChoice));
        }

        if (header != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("header")), "%" + header.toLowerCase() + "%"));
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

        return questions.stream()
                .map(questionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<QuestionDTO> getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(questionMapper::toDTO);
    }

    @Transactional
    public List<AnswerKeyDTO> getAnswerKeys(List<Long> questionsIds) {

        List<AnswerKeyDTO> pairs = new ArrayList<>();
        for (Long id : questionsIds) {
            Optional<QuestionDTO> dto = questionRepository.findById(id).map(questionMapper::toDTO);
            if (dto.isPresent()) {
                pairs.add(new AnswerKeyDTO(dto.get().getId(), dto.get().getAnswerId()));
            }

        }
        System.out.println(pairs);
        return pairs;
    }

    @Transactional
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) throws RuntimeException {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with ID " + id));

        question.setMultipleChoice(questionDTO.isMultipleChoice());
        question.setNumberLines(questionDTO.getNumberLines());
        question.setPersonId(questionDTO.getPersonId());
        question.setHeader(questionDTO.getHeader());
        question.setHeader_image(questionDTO.getHeader_image());
        question.setEnable(questionDTO.isEnable());
        question.setAccessLevel(questionDTO.getAccessLevel());
        question.setEducationLevel(questionDTO.getEducationLevel());

        QuestionServiceHelper.setTags(questionDTO, question, tagRepository);

        Set<Alternative> updatedAlternatives = new HashSet<>();
        List<AlternativeDTO> correctAlternatives = questionDTO.getAlternatives().stream()
                .filter(AlternativeDTO::getIsCorrect)
                .collect(Collectors.toList());

        if (correctAlternatives.isEmpty()) {
            throw new IllegalArgumentException("No correct alternative provided.");
        }

        if (correctAlternatives.size() > 1) {
            throw new IllegalArgumentException("More than one correct alternative provided.");
        }
        for (AlternativeDTO alternativeDTO : questionDTO.getAlternatives()) {
            if (alternativeDTO.getId() != null) {
                Alternative existingAlternative = alternativeRepository.findById(alternativeDTO.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Alternative not found"));

                existingAlternative.setDescription(alternativeDTO.getDescription());
                existingAlternative.setExplanation(alternativeDTO.getExplanation());
                existingAlternative.setImagePath(alternativeDTO.getImagePath());
                existingAlternative.setIsCorrect(alternativeDTO.getIsCorrect());
                updatedAlternatives.add(alternativeRepository.save(existingAlternative));
            } else {
                Alternative newAlternative = Alternative.builder()
                        .description(alternativeDTO.getDescription())
                        .imagePath(alternativeDTO.getImagePath())
                        .isCorrect(alternativeDTO.getIsCorrect())
                        .question(question)
                        .build();
                updatedAlternatives.add(alternativeRepository.save(newAlternative));
            }
        }

        if (correctAlternatives.size() == 1) {
            Alternative correctAlternative = updatedAlternatives.stream()
                    .filter(Alternative::getIsCorrect)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No correct alternative provided."));
            question.setAnswerId(correctAlternative.getId());
        }

        questionRepository.save(question);
        questionRepository.flush();

        entityManager.refresh(question);

        Question updatedQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with ID " + id));

        return questionMapper.toDTO(updatedQuestion);
    }

    public void deleteQuestion(Long id) {
        questionRepository.findById(id)
                .map(question -> {
                    question.setEnable(false);

                    Question updatedQuestion = questionRepository.save(question);
                    return updatedQuestion;
                })
                .orElseThrow(() -> new RuntimeException("Question not found with ID " + id));
    }

}

class QuestionServiceHelper {

    public static void setTags(QuestionDTO questionDTO, Question question, TagRepository tagRepository) {
        if (questionDTO.getTagIds() != null && !questionDTO.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : questionDTO.getTagIds()) {
                Optional<Tag> tagOpt = tagRepository.findById(tagId);
                tagOpt.ifPresent(tags::add);
            }
            question.setTags(tags);
        }
    }
}
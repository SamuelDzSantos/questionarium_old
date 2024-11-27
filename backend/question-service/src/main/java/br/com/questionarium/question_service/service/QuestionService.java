package br.com.questionarium.question_service.service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Optional<QuestionDTO> getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(questionMapper::toDTO);
    }

    @Transactional
    public List<AnswerKeyDTO> getAnswerKeys(List<Long> questionsIds){
        
        List<AnswerKeyDTO> pairs = new ArrayList<>();
        for (Long id : questionsIds ) {
            Optional<QuestionDTO> dto = questionRepository.findById(id).map(questionMapper::toDTO);
            if(dto.isPresent()){
                pairs.add(new AnswerKeyDTO(dto.get().getId(), dto.get().getAnswerId()));
            }

        }
        System.out.println(pairs);
        return pairs;
    }

    @Transactional
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) throws RuntimeException {
        
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with ID " + id));
    
        question.setMultipleChoice(questionDTO.isMultipleChoice());
        question.setNumberLines(questionDTO.getNumberLines());
        question.setPersonId(questionDTO.getPersonId());
        question.setHeaderId(questionDTO.getHeaderId());
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

        Question updatedQuestion = questionRepository.save(question);
        System.out.println(updatedQuestion);
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
package br.com.questionarium.question_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.questionarium.question_service.dto.AlternativeDTO;
import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.mapper.QuestionMapper;
import br.com.questionarium.question_service.model.Alternative;
import br.com.questionarium.question_service.model.Question;
import br.com.questionarium.question_service.repository.AlternativeRepository;
import br.com.questionarium.question_service.repository.QuestionRepository;
import br.com.questionarium.question_service.types.QuestionAccessLevel;
import br.com.questionarium.question_service.types.QuestionEducationLevel;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final AlternativeRepository alternativeRepository;
    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository,
            AlternativeRepository alternativeRepository,
            QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.alternativeRepository = alternativeRepository;
        this.questionMapper = questionMapper;
    }

    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        logger.info("Criando nova questão para usuário {}", questionDTO.getUserId());

        // Extrai as alternativas do DTO
        List<AlternativeDTO> alternativesDto = questionDTO.getAlternatives();
        List<AlternativeDTO> correctAlternatives = alternativesDto.stream()
                .filter(AlternativeDTO::getIsCorrect)
                .collect(Collectors.toList());

        // Se não houver exatamente uma correta, lança 400 Bad Request
        if (correctAlternatives.size() != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Deve existir exatamente uma alternativa correta. Encontradas: "
                            + correctAlternatives.size());
        }

        // Mapeia DTO para entidade
        Question question = questionMapper.toEntity(questionDTO);

        // Preenche o conjunto de alternativas
        Set<Alternative> alternatives = alternativesDto.stream()
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

        // Salva para gerar IDs
        Question saved = questionRepository.save(question);

        // Determina e persiste o gabarito (answerId)
        Alternative correct = saved.getAlternatives().stream()
                .filter(Alternative::getIsCorrect)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Alternativa correta não encontrada após salvar."));
        saved.setAnswerId(correct.getId());

        // Re-salva com o answerId definido
        Question updated = questionRepository.save(saved);

        // Retorna o DTO da questão criada
        return questionMapper.toDto(updated);
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getFilteredQuestions(Long userId,
            Boolean multipleChoice,
            List<Long> tagIds,
            Integer accessLevel,
            Integer educationLevel,
            String header) {
        Specification<Question> spec = Specification.where((root, query, cb) -> cb.isTrue(root.get("enable")));

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

        return questionRepository.findAll(spec).stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getFilteredQuestionsAsAdmin(Long userId,
            Boolean multipleChoice,
            List<Long> tagIds,
            Integer accessLevel,
            Integer educationLevel,
            String header) {
        Specification<Question> spec = Specification.where(null);

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

        return questionRepository.findAll(spec).stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<QuestionDTO> getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(questionMapper::toDto);
    }

    public Optional<QuestionDTO> getQuestionByIdForUser(Long id, Long userId) {
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
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO, Long userId) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questão não encontrada: " + id));
        if (!q.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Permissão negada.");
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
    public void deleteQuestion(Long id, Long userId) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questão não encontrada: " + id));
        if (!q.getUserId().equals(userId)) {
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

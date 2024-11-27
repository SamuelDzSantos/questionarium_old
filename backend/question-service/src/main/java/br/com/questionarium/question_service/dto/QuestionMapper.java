package br.com.questionarium.question_service.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.questionarium.question_service.model.Alternative;
import br.com.questionarium.question_service.model.Question;
import br.com.questionarium.question_service.model.Tag;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "alternatives", source = "alternatives")
    @Mapping(source = "tags", target = "tagIds", qualifiedByName = "mapTagsToIds")
    QuestionDTO toDTO(Question question);

    @Mapping(source = "tagIds", target = "tags", qualifiedByName = "mapIdsToTags")
    @Mapping(target = "alternatives", source = "alternatives")
    Question toEntity(QuestionDTO questionDTO);

    @Mapping(target = "question", source = "question_id", qualifiedByName = "mapQuestionIdToQuestion")
    Alternative toEntity(AlternativeDTO alternativeDTO);

    @Mapping(target = "question_id", source = "question.id")
    AlternativeDTO toDTO(Alternative alternative);

    // Custom mapping methods
    @Named("mapQuestionIdToQuestion")
    default Question mapQuestionIdToQuestion(Long questionId) {
        if (questionId == null) {
            return null;
        }
        Question question = new Question();
        question.setId(questionId);
        return question;
    }

    @Named("mapTagsToIds")
    default Set<Long> mapTagsToIds(Set<Tag> tags) {
        return tags != null ? tags.stream().map(Tag::getId).collect(Collectors.toSet()) : new HashSet<>();
    }

    @Named("mapIdsToTags")
    default Set<Tag> mapIdsToTags(Set<Long> tagIds) {
        if (tagIds == null) return new HashSet<>();
        return tagIds.stream().map(id -> {
            Tag tag = new Tag();
            tag.setId(id);
            return tag;
        }).collect(Collectors.toSet());
    }

    @Named("mapAlternativesToIds")
    default Set<Long> mapAlternativesToIds(Set<Alternative> alternatives) {
        return alternatives != null ? alternatives.stream().map(Alternative::getId).collect(Collectors.toSet()) : new HashSet<>();
    }

    @Named("mapIdsToAlternatives")
    default Set<Alternative> mapIdsToAlternatives(Set<Long> alternativesIds) {
        if (alternativesIds == null) return new HashSet<>();
        return alternativesIds.stream().map(id -> {
            Alternative alternative = new Alternative();
            alternative.setId(id);
            return alternative;
        }).collect(Collectors.toSet());
    }
}

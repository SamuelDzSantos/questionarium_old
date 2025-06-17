package br.com.questionarium.question_service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.questionarium.question_service.dto.QuestionDTO;
import br.com.questionarium.question_service.model.Question;
import br.com.questionarium.question_service.model.Tag;

@Mapper(componentModel = "spring", uses = { AlternativeMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {

    @Mapping(source = "tags", target = "tagIds")
    QuestionDTO toDto(Question entity);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "alternatives", ignore = true)
    @Mapping(target = "creationDateTime", ignore = true)
    @Mapping(target = "updateDateTime", ignore = true)
    @Mapping(target = "answerId", ignore = true)
    Question toEntity(QuestionDTO dto);

    default List<Long> tagsToTagIds(Set<Tag> tags) {
        if (tags == null) {
            return List.of();
        }
        return tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());
    }
}

package br.com.questionarium.question_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.questionarium.question_service.dto.TagDTO;
import br.com.questionarium.question_service.model.Tag;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    TagDTO toDto(Tag entity);

    Tag toEntity(TagDTO dto);
}
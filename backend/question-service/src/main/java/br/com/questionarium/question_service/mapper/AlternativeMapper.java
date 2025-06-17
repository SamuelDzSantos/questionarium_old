package br.com.questionarium.question_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.questionarium.question_service.dto.AlternativeDTO;
import br.com.questionarium.question_service.model.Alternative;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlternativeMapper {
    AlternativeDTO toDto(Alternative entity);

    Alternative toEntity(AlternativeDTO dto);
}

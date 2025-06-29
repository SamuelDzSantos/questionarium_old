package com.github.questionarium.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.questionarium.interfaces.DTOs.QuestionDTO;
import com.github.questionarium.model.QuestionSnapshot;

@Mapper(componentModel = "spring", uses = { AlternativeSnapshotMapper.class })
public interface QuestionSnapshotMapper {
    QuestionDTO toDto(QuestionSnapshot entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appliedAssessment", ignore = true)
    @Mapping(target = "recordAssessment", ignore = true)
    QuestionSnapshot toEntity(QuestionDTO dto);
}

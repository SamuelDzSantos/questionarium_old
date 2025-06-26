package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.questionarium.assessment_service.dto.QuestionDTO;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

@Mapper(componentModel = "spring", uses = { AlternativeSnapshotMapper.class })
public interface QuestionSnapshotMapper {
    QuestionDTO toDto(QuestionSnapshot entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appliedAssessment", ignore = true)
    @Mapping(target = "recordAssessment", ignore = true)
    QuestionSnapshot toEntity(QuestionDTO dto);
}

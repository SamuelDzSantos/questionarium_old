package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.questionarium.assessment_service.dto.QuestionDTO;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

@Mapper(componentModel = "spring", uses = {
        AlternativeSnapshotMapper.class }, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface QuestionSnapshotMapper {
    QuestionDTO toDto(QuestionSnapshot entity);

    QuestionSnapshot toEntity(QuestionDTO dto);
}

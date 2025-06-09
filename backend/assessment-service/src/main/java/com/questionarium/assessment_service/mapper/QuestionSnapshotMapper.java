package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;

import com.questionarium.assessment_service.dto.QuestionSnapshotDTO;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

@Mapper(componentModel = "spring", uses = { AlternativeSnapshotMapper.class })
public interface QuestionSnapshotMapper {
    QuestionSnapshotDTO toDto(QuestionSnapshot entity);

    QuestionSnapshot toEntity(QuestionSnapshotDTO dto);
}
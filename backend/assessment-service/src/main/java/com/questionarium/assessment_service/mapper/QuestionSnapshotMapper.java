package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import com.questionarium.assessment_service.dto.QuestionDTO;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

@Mapper(componentModel = "spring", uses = { AlternativeSnapshotMapper.class })
public interface QuestionSnapshotMapper {

    // Entidade embeddable → DTO
    QuestionDTO toDto(QuestionSnapshot entity);

    // DTO → entidade embeddable
    QuestionSnapshot toEntity(QuestionDTO dto);
}

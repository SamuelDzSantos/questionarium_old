package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;

import com.questionarium.assessment_service.dto.AlternativeSnapshotDTO;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;

@Mapper(componentModel = "spring")
public interface AlternativeSnapshotMapper {
    AlternativeSnapshotDTO toDto(AlternativeSnapshot entity);

    AlternativeSnapshot toEntity(AlternativeSnapshotDTO dto);
}

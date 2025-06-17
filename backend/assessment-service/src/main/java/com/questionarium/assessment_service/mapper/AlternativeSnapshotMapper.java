package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;
import com.questionarium.assessment_service.dto.AlternativeDTO;

@Mapper(componentModel = "spring")
public interface AlternativeSnapshotMapper {

    @Mapping(source = "alternative", target = "id")
    @Mapping(source = "position", target = "alternativeOrder")
    AlternativeDTO toDto(AlternativeSnapshot entity);

    @Mapping(source = "id", target = "alternative")
    @Mapping(source = "alternativeOrder", target = "position")
    AlternativeSnapshot toEntity(AlternativeDTO dto);
}

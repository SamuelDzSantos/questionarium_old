package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.questionarium.assessment_service.dto.AlternativeDTO;
import com.questionarium.assessment_service.snapshot.AlternativeSnapshot;

@Mapper(componentModel = "spring")
public interface AlternativeSnapshotMapper {

    @Mapping(source = "originalAlternativeId", target = "id")
    @Mapping(source = "position",                  target = "alternativePosition")
    AlternativeDTO toDto(AlternativeSnapshot entity);

    @Mapping(source = "id",                         target = "originalAlternativeId")
    @Mapping(source = "alternativePosition",        target = "position")
    @Mapping(target = "questionSnapshot",           ignore = true)
    AlternativeSnapshot toEntity(AlternativeDTO dto);
}

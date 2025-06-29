package com.github.questionarium.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.questionarium.interfaces.DTOs.AlternativeDTO;
import com.github.questionarium.model.AlternativeSnapshot;

@Mapper(componentModel = "spring")
public interface AlternativeSnapshotMapper {

    @Mapping(source = "alternative", target = "id")
    @Mapping(source = "position", target = "alternativeOrder")
    AlternativeDTO toDto(AlternativeSnapshot entity);

    @Mapping(source = "id", target = "alternative")
    @Mapping(source = "alternativeOrder", target = "position")
    AlternativeSnapshot toEntity(AlternativeDTO dto);
}

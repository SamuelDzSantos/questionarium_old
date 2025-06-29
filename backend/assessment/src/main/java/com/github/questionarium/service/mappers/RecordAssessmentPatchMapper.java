package com.github.questionarium.service.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.github.questionarium.interfaces.DTOs.PatchRecordAssessmentRequestDTO;
import com.github.questionarium.model.RecordAssessment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RecordAssessmentPatchMapper {

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "studentAnswerKey", target = "studentAnswerKey")
    void updateStudentKeyFromDto(
            PatchRecordAssessmentRequestDTO dto,
            @MappingTarget RecordAssessment entity);
}

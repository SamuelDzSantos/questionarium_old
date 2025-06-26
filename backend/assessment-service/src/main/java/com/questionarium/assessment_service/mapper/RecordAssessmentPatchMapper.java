package com.questionarium.assessment_service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.questionarium.assessment_service.dto.PatchRecordAssessmentRequestDTO;
import com.questionarium.assessment_service.model.RecordAssessment;

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

package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.questionarium.assessment_service.dto.RecordAssessmentPublicDTO;
import com.questionarium.assessment_service.model.RecordAssessment;

@Mapper(componentModel = "spring", uses = { AppliedAssessmentMapper.class })
public interface RecordAssessmentPublicMapper {

    @Mapping(source = "appliedAssessment.id", target = "appliedAssessmentId")
    RecordAssessmentPublicDTO toDto(RecordAssessment entity);
}

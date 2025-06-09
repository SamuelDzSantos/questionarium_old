package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;

import com.questionarium.assessment_service.dto.RecordAssessmentPublicDTO;
import com.questionarium.assessment_service.model.RecordAssessment;

@Mapper(componentModel = "spring", uses = { AppliedAssessmentMapper.class })
public interface RecordAssessmentPublicMapper {

    RecordAssessmentPublicDTO toDto(RecordAssessment entity);
}

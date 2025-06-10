package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.questionarium.assessment_service.dto.CreateAssessmentModelRequestDTO;
import com.questionarium.assessment_service.dto.AssessmentModelDTO;
import com.questionarium.assessment_service.dto.QuestionWeightDTO;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.model.QuestionWeight;

@Mapper(componentModel = "spring")
public interface AssessmentModelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "creationDateTime", ignore = true)
    @Mapping(target = "updateDateTime", ignore = true)
    AssessmentModel toEntity(CreateAssessmentModelRequestDTO dto);

    AssessmentModelDTO toDto(AssessmentModel entity);

    QuestionWeight toEntity(QuestionWeightDTO dto);

    QuestionWeightDTO toDto(QuestionWeight entity);
}

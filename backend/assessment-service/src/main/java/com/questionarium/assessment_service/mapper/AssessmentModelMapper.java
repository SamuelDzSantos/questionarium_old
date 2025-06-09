package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;

import com.questionarium.assessment_service.dto.AssessmentModelDTO;
import com.questionarium.assessment_service.dto.QuestionWeightDTO;
import com.questionarium.assessment_service.model.AssessmentModel;
import com.questionarium.assessment_service.model.QuestionWeight;

@Mapper(componentModel = "spring")
public interface AssessmentModelMapper {
    AssessmentModel toEntity(AssessmentModelDTO dto);

    AssessmentModelDTO toDto(AssessmentModel entity);

    QuestionWeight toEntity(QuestionWeightDTO dto);

    QuestionWeightDTO toDto(QuestionWeight entity);

}

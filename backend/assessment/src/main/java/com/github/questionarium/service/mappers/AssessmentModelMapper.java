package com.github.questionarium.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.questionarium.interfaces.DTOs.AssessmentModelDTO;
import com.github.questionarium.interfaces.DTOs.CreateAssessmentModelRequestDTO;
import com.github.questionarium.interfaces.DTOs.QuestionWeightDTO;
import com.github.questionarium.model.AssessmentModel;
import com.github.questionarium.model.QuestionWeight;

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

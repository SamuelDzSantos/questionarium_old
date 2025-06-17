package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.questionarium.assessment_service.dto.AppliedAssessmentDTO;
import com.questionarium.assessment_service.dto.ApplyAssessmentRequestDTO;
import com.questionarium.assessment_service.model.AppliedAssessment;

@Mapper(componentModel = "spring", uses = {
        QuestionSnapshotMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppliedAssessmentMapper {

    @Mapping(source = "questionSnapshots", target = "questionSnapshots")
    AppliedAssessmentDTO toDto(AppliedAssessment entity);

    @Mapping(target = "id", ignore = true)
    // removido: @Mapping(source = "description", target = "description")
    @Mapping(source = "applicationDate", target = "applicationDate")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "shuffleQuestions", target = "shuffleQuestions")
    AppliedAssessment toEntity(ApplyAssessmentRequestDTO dto);
}

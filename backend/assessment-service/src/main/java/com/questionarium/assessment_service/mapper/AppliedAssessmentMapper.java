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

    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "institution", target = "institution")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "course", target = "course")
    @Mapping(source = "classroom", target = "classroom")
    @Mapping(source = "professor", target = "professor")
    @Mapping(source = "instructions", target = "instructions")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "creationDateTime", target = "creationDateTime")
    @Mapping(source = "updateDateTime", target = "updateDateTime")
    @Mapping(source = "applicationDate", target = "applicationDate")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "shuffleQuestions", target = "shuffleQuestions")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "totalScore", target = "totalScore")
    @Mapping(source = "correctAnswerKey", target = "correctAnswerKey")
    @Mapping(source = "questionSnapshots", target = "questionSnapshots")
    AppliedAssessmentDTO toDto(AppliedAssessment entity);

    /** DTO de requisição → Entidade */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "classroom", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "instructions", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "creationDateTime", ignore = true)
    @Mapping(target = "updateDateTime", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "totalScore", ignore = true)
    @Mapping(target = "correctAnswerKey", ignore = true)
    @Mapping(target = "questionSnapshots", ignore = true)
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "applicationDate", source = "applicationDate")
    @Mapping(target = "shuffleQuestions", source = "shuffleQuestions")
    AppliedAssessment toEntity(ApplyAssessmentRequestDTO dto);
}

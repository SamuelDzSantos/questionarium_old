package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.questionarium.assessment_service.dto.AppliedAssessmentDTO;
import com.questionarium.assessment_service.dto.ApplyAssessmentRequestDTO;
import com.questionarium.assessment_service.model.AppliedAssessment;

@Mapper(componentModel = "spring", uses = { QuestionSnapshotMapper.class })
public interface AppliedAssessmentMapper {

    // entidade → DTO de resposta
    @Mapping(target = "questionSnapshots", source = "questionSnapshots")
    AppliedAssessmentDTO toDto(AppliedAssessment entity);

    // DTO de requisição → entidade (service injeta userId, descrição, score, snapshots…)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "totalScore", ignore = true)
    @Mapping(target = "creationDateTime", ignore = true)
    @Mapping(target = "updateDateTime", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "classroom", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "instructions", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "correctAnswerKey", ignore = true)
    @Mapping(target = "questionSnapshots", ignore = true)
    AppliedAssessment toEntity(ApplyAssessmentRequestDTO dto);
}

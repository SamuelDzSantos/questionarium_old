package com.questionarium.assessment_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.questionarium.assessment_service.dto.RecordAssessmentPublicDTO;
import com.questionarium.assessment_service.model.RecordAssessment;

@Mapper(componentModel = "spring", uses = { QuestionSnapshotMapper.class })
public interface RecordAssessmentPublicMapper {
    @Mapping(source = "correctAnswerKeyLetter", target = "correctAnswerKeyLetter")
    RecordAssessmentPublicDTO toDto(RecordAssessment entity);

    List<RecordAssessmentPublicDTO> toDto(List<RecordAssessment> entities);
}
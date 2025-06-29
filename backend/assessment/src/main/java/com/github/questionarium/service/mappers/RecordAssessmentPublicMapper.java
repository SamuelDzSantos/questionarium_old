package com.github.questionarium.service.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.questionarium.interfaces.DTOs.RecordAssessmentPublicDTO;
import com.github.questionarium.model.RecordAssessment;

@Mapper(componentModel = "spring", uses = { QuestionSnapshotMapper.class })
public interface RecordAssessmentPublicMapper {
    @Mapping(source = "correctAnswerKeyLetter", target = "correctAnswerKeyLetter")
    @Mapping(source = "studentAnswerKey", target = "studentAnswerKey")
    RecordAssessmentPublicDTO toDto(RecordAssessment entity);

    List<RecordAssessmentPublicDTO> toDto(List<RecordAssessment> entities);
}
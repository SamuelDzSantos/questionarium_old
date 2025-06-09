package com.questionarium.assessment_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.questionarium.assessment_service.dto.CreateRecordAssessmentRequestDTO;
import com.questionarium.assessment_service.dto.RecordAssessmentDTO;
import com.questionarium.assessment_service.model.RecordAssessment;

@Mapper(componentModel = "spring", uses = { QuestionSnapshotMapper.class })
public interface RecordAssessmentMapper {

    @Mappings({
            @Mapping(source = "appliedAssessment.id", target = "appliedAssessmentId")
    })
    RecordAssessmentDTO toDto(RecordAssessment entity);

    List<RecordAssessmentDTO> toDto(List<RecordAssessment> entities);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "appliedAssessment", ignore = true),
            @Mapping(target = "instanceIndex", ignore = true),
            @Mapping(target = "questionOrder", ignore = true),
            @Mapping(target = "questionSnapshots", ignore = true),
            @Mapping(target = "totalScore", ignore = true),
            @Mapping(target = "obtainedScore", ignore = true),
            @Mapping(target = "correctAnswerKey", ignore = true),
            @Mapping(target = "studentAnswerKey", ignore = true),
            @Mapping(target = "creationDateTime", ignore = true),
            @Mapping(target = "updateDateTime", ignore = true),
            @Mapping(target = "active", ignore = true)
    })
    RecordAssessment toEntity(CreateRecordAssessmentRequestDTO dto);
}

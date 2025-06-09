package com.questionarium.assessment_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.questionarium.assessment_service.dto.CreateRecordAssessmentRequestDTO;
import com.questionarium.assessment_service.dto.RecordAssessmentDTO;
import com.questionarium.assessment_service.model.RecordAssessment;

@Mapper(componentModel = "spring", imports = { java.util.stream.Collectors.class })
public interface RecordAssessmentMapper {

    /** Entity → DTO */
    @Mapping(source = "appliedAssessment.id", target = "appliedAssessmentId")
    RecordAssessmentDTO toDto(RecordAssessment entity);

    List<RecordAssessmentDTO> toDto(List<RecordAssessment> entities);

    /**
     * RequestDTO → só os campos iniciais da entidade RecordAssessment (o resto o
     * service popula)
     */
    RecordAssessment toEntity(CreateRecordAssessmentRequestDTO dto);
}
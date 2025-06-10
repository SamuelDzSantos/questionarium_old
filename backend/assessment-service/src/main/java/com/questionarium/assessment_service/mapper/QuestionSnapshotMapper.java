package com.questionarium.assessment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.questionarium.assessment_service.dto.QuestionDTO;
import com.questionarium.assessment_service.snapshot.QuestionSnapshot;

@Mapper(componentModel = "spring", uses = { AlternativeSnapshotMapper.class })
public interface QuestionSnapshotMapper {

    // entidade → DTO: mapeia originalQuestionId → id no DTO
    @Mapping(source = "originalQuestionId", target = "id")
    QuestionDTO toDto(QuestionSnapshot entity);

    // DTO → entidade: ignora campos gerenciados pelo serviço ou gerados automaticamente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originalQuestionId", ignore = true)
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "recordAssessment", ignore = true)
    QuestionSnapshot toEntity(QuestionDTO dto);
}

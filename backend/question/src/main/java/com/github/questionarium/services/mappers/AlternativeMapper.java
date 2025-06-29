package com.github.questionarium.services.mappers;

import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.AlternativeDTO;
import com.github.questionarium.model.Alternative;

@Service
public class AlternativeMapper {

    public AlternativeDTO toDto(Alternative entity) {
        return new AlternativeDTO(entity.getId(), entity.getDescription(), entity.getImagePath(), entity.getIsCorrect(),
                entity.getExplanation(), entity.getAlternativeOrder());
    }

    public Alternative toEntity(AlternativeDTO dto) {
        return new Alternative(dto.getId(), dto.getDescription(), dto.getImagePath(), dto.getIsCorrect(),
                dto.getExplanation(), dto.getAlternativeOrder(), null);
    }
}

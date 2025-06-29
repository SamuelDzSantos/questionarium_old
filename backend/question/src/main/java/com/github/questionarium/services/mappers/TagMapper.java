package com.github.questionarium.services.mappers;

import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.TagDTO;
import com.github.questionarium.model.Tag;

@Service
public class TagMapper {

    public TagDTO toDto(Tag entity) {
        return new TagDTO(entity.getId(), entity.getName(), entity.getDescription());
    }

    public Tag toEntity(TagDTO dto) {
        return new Tag(dto.getId(), dto.getName(), dto.getDescription());
    }
}
package com.github.questionarium.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.questionarium.config.exception.TagNotFoundException;
import com.github.questionarium.interfaces.DTOs.TagDTO;
import com.github.questionarium.model.Tag;
import com.github.questionarium.repository.TagRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagDTO createTag(TagDTO tagDTO) {
        log.info("Criando nova tag com nome '{}'", tagDTO.getName());
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setDescription(tagDTO.getDescription());
        tag = tagRepository.save(tag);
        tagDTO.setId(tag.getId());
        log.info("Tag criada com ID {}", tagDTO.getId());
        return tagDTO;
    }

    public List<TagDTO> getAllTags() {
        log.info("Buscando todas as tags");
        List<TagDTO> result = tagRepository.findAll()
                .stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName(), tag.getDescription()))
                .toList();
        log.info("Foram encontradas {} tags", result.size());
        return result;
    }

    public TagDTO getTagById(Long id) {
        log.info("Buscando tag por ID {}", id);
        Optional<Tag> tagOpt = tagRepository.findById(id);
        if (tagOpt.isPresent()) {
            Tag tag = tagOpt.get();
            log.info("Tag {} encontrada: {}", id, tag.getName());
            return new TagDTO(tag.getId(), tag.getName(), tag.getDescription());
        } else {
            log.error("Tag não encontrada com ID {}", id);
            throw new TagNotFoundException(id);
        }
    }

    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        log.info("Atualizando tag {} com dados {}", id, tagDTO);
        Optional<Tag> tagOpt = tagRepository.findById(id);
        if (tagOpt.isPresent()) {
            Tag tag = tagOpt.get();
            tag.setName(tagDTO.getName());
            tag.setDescription(tagDTO.getDescription());
            tag = tagRepository.save(tag);
            tagDTO.setId(tag.getId());
            log.info("Tag {} atualizada com sucesso", id);
            return tagDTO;
        } else {
            log.error("Tag não encontrada para atualizar com ID {}", id);
            throw new TagNotFoundException(id);
        }
    }

    public void deleteTag(Long id) {
        log.info("Excluindo tag com ID {}", id);
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            log.info("Tag {} excluída com sucesso", id);
        } else {
            log.error("Tag não encontrada para exclusão com ID {}", id);
            throw new TagNotFoundException(id);
        }
    }
}

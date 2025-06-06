package br.com.questionarium.question_service.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.questionarium.question_service.dto.TagDTO;
import br.com.questionarium.question_service.model.Tag;
import br.com.questionarium.question_service.repository.TagRepository;

@Service
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagDTO createTag(TagDTO tagDTO) {
        logger.info("Criando nova tag com nome '{}'", tagDTO.getName());
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setDescription(tagDTO.getDescription());
        tag = tagRepository.save(tag);
        tagDTO.setId(tag.getId());
        logger.info("Tag criada com ID {}", tagDTO.getId());
        return tagDTO;
    }

    public List<TagDTO> getAllTags() {
        logger.info("Buscando todas as tags");
        List<TagDTO> result = tagRepository.findAll()
                .stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName(), tag.getDescription()))
                .toList();
        logger.info("Foram encontradas {} tags", result.size());
        return result;
    }

    public TagDTO getTagById(Long id) {
        logger.info("Buscando tag por ID {}", id);
        Optional<Tag> tagOpt = tagRepository.findById(id);
        if (tagOpt.isPresent()) {
            Tag tag = tagOpt.get();
            logger.info("Tag {} encontrada: {}", id, tag.getName());
            return new TagDTO(tag.getId(), tag.getName(), tag.getDescription());
        } else {
            logger.error("Tag não encontrada com ID {}", id);
            throw new RuntimeException("Tag not found with ID " + id);
        }
    }

    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        logger.info("Atualizando tag {} com dados {}", id, tagDTO);
        Optional<Tag> tagOpt = tagRepository.findById(id);
        if (tagOpt.isPresent()) {
            Tag tag = tagOpt.get();
            tag.setName(tagDTO.getName());
            tag.setDescription(tagDTO.getDescription());
            tag = tagRepository.save(tag);
            tagDTO.setId(tag.getId());
            logger.info("Tag {} atualizada com sucesso", id);
            return tagDTO;
        } else {
            logger.error("Tag não encontrada para atualizar com ID {}", id);
            throw new RuntimeException("Tag not found with ID " + id);
        }
    }

    public void deleteTag(Long id) {
        logger.info("Excluindo tag com ID {}", id);
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            logger.info("Tag {} excluída com sucesso", id);
        } else {
            logger.error("Tag não encontrada para exclusão com ID {}", id);
            throw new RuntimeException("Tag not found with ID " + id);
        }
    }
}

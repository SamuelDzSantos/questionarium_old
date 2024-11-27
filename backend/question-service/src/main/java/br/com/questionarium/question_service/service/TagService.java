package br.com.questionarium.question_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.questionarium.question_service.dto.TagDTO;
import br.com.questionarium.question_service.model.Tag;
import br.com.questionarium.question_service.repository.TagRepository;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setDescription(tagDTO.getDescription());
        tag = tagRepository.save(tag);

        tagDTO.setId(tag.getId());
        return tagDTO;
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName(), tag.getDescription()))
                .toList();
    }

    public TagDTO getTagById(Long id) {
        Optional<Tag> tagOpt = tagRepository.findById(id);
        if (tagOpt.isPresent()) {
            Tag tag = tagOpt.get();
            return new TagDTO(tag.getId(), tag.getName(), tag.getDescription());
        } else {
            throw new RuntimeException("Tag not found with ID " + id);
        }
    }

    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Optional<Tag> tagOpt = tagRepository.findById(id);
        if (tagOpt.isPresent()) {
            Tag tag = tagOpt.get();
            tag.setName(tagDTO.getName());
            tag.setDescription(tagDTO.getDescription());
            tag = tagRepository.save(tag);

            tagDTO.setId(tag.getId());
            return tagDTO;
        } else {
            throw new RuntimeException("Tag not found with ID " + id);
        }
    }

    public void deleteTag(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
        } else {
            throw new RuntimeException("Tag not found with ID " + id);
        }
    }
}
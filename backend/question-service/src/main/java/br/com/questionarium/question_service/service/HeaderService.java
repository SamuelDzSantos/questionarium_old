package br.com.questionarium.question_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.questionarium.question_service.dto.HeaderDTO;
import br.com.questionarium.question_service.model.Header;
import br.com.questionarium.question_service.repository.HeaderRepository;

@Service
public class HeaderService {

    private final HeaderRepository headerRepository;

    public HeaderService(HeaderRepository headerRepository) {
        this.headerRepository = headerRepository;
    }

    public HeaderDTO createHeader(HeaderDTO headerDTO) {
        Header header = new Header();
        header.setContent(headerDTO.getContent());
        header.setImagePath(headerDTO.getImagePath());
        header = headerRepository.save(header);

        headerDTO.setId(header.getId());
        return headerDTO;
    }

    public List<HeaderDTO> getAllHeaders() {
        return headerRepository.findAll()
                .stream()
                .map(header -> new HeaderDTO(header.getId(), header.getContent(), header.getImagePath()))
                .toList();
    }

    public HeaderDTO getHeaderById(Long id) {
        Optional<Header> headerOpt = headerRepository.findById(id);
        if (headerOpt.isPresent()) {
            Header header = headerOpt.get();
            return new HeaderDTO(header.getId(), header.getContent(), header.getImagePath());
        } else {
            throw new RuntimeException("Header not found with ID " + id);
        }
    }

    public HeaderDTO updateHeader(Long id, HeaderDTO headerDTO) {
        Optional<Header> headerOpt = headerRepository.findById(id);
        if (headerOpt.isPresent()) {
            Header header = headerOpt.get();
            header.setContent(headerDTO.getContent());
            header.setImagePath(headerDTO.getImagePath());
            header = headerRepository.save(header);

            headerDTO.setId(header.getId());
            return headerDTO;
        } else {
            throw new RuntimeException("Header not found with ID " + id);
        }
    }

    public void deleteHeader(Long id) {
        if (headerRepository.existsById(id)) {
            headerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Header not found with ID " + id);
        }
    }
}
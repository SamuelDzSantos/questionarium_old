package br.com.questionarium.evaluation_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.questionarium.evaluation_service.models.EvaluationHeader;
import br.com.questionarium.evaluation_service.repositories.EvaluationHeaderRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationHeaderService {

    private final EvaluationHeaderRepository repository;

    // CRIAR HEADER
    public EvaluationHeader createHeader(EvaluationHeader evaluationHeader) {
        return repository.save(evaluationHeader);
    }

    // BUSCAR 1 HEADER POR ID
    public EvaluationHeader getHeaderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Header not found with ID: " + id));
    }

    // BUSCA TODOS HEADERS DE UM USER
    public List<EvaluationHeader> getAllHeadersByUser(Long userId) {
        return repository.findAllByUserId(userId);
    }

    // ATUALIZA HEADER POR ID
    public EvaluationHeader updateHeader(Long id, EvaluationHeader updatedHeader) {
        EvaluationHeader existingHeader = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Header not found with ID: " + id));

        if (updatedHeader.getInstitution() != null)
            existingHeader.setInstitution(updatedHeader.getInstitution());
        if (updatedHeader.getDepartment() != null)
            existingHeader.setDepartment(updatedHeader.getDepartment());
        if (updatedHeader.getCourse() != null)
            existingHeader.setCourse(updatedHeader.getCourse());
        if (updatedHeader.getClassroom() != null)
            existingHeader.setClassroom(updatedHeader.getClassroom());
        if (updatedHeader.getProfessor() != null)
            existingHeader.setProfessor(updatedHeader.getProfessor());
        if (updatedHeader.getInstructions() != null)
            existingHeader.setInstructions(updatedHeader.getInstructions());
        if (updatedHeader.getImage() != null)
            existingHeader.setImage(updatedHeader.getImage());
        if (updatedHeader.getCreationDate() != null)
            existingHeader.setCreationDate(updatedHeader.getCreationDate());

        return repository.save(existingHeader);
    }

    // DELETA HEADER POR ID
    public void deleteHeader(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Header not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}

package br.com.questionarium.assessment_service.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.questionarium.assessment_service.dto.AssessmentHeaderUserDTO;
import br.com.questionarium.assessment_service.models.AssessmentHeader;
import br.com.questionarium.assessment_service.repositories.AssessmentHeaderRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssessmentHeaderService {

    private final AssessmentHeaderRepository repository;

    // CRIAR HEADER
    public AssessmentHeader createHeader(AssessmentHeader assessmentHeader) {
        assessmentHeader.setCreationDate(LocalDate.now());
        return repository.save(assessmentHeader);
    }

    // BUSCAR 1 HEADER POR ID
    public AssessmentHeader getHeaderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Header not found with ID: " + id));
    }

    // BUSCA TODOS HEADERS DE UM USER
    public List<AssessmentHeaderUserDTO> getAllHeadersByUser(Long userId) {
        List<AssessmentHeader> headers = repository.findAllHeadersByUserId(userId);
        return headers.stream()
                .map(header -> new AssessmentHeaderUserDTO(
                        header.getId(),
                        header.getInstitution(),
                        header.getDepartment(),
                        header.getCourse(),
                        header.getClassroom(),
                        header.getProfessor(),
                        header.getCreationDate(),
                        header.getUserId()))
                .collect(Collectors.toList());
    }

    // ATUALIZA HEADER POR ID
    public AssessmentHeader updateHeader(Long id, AssessmentHeader updatedHeader) {
        AssessmentHeader existingHeader = repository.findById(id)
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

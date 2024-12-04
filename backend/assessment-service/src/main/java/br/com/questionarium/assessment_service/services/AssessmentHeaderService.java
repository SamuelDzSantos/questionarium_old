package br.com.questionarium.assessment_service.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.questionarium.assessment_service.dto.AssessmentHeaderDTO;
import br.com.questionarium.assessment_service.dto.AssessmentHeaderUserDTO;
import br.com.questionarium.assessment_service.models.AssessmentHeader;
import br.com.questionarium.assessment_service.repositories.AssessmentHeaderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssessmentHeaderService {

    private final AssessmentHeaderRepository assessmentHeaderRepository;

    // CRIAR HEADER
    public AssessmentHeader createHeader(AssessmentHeaderDTO assessmentHeaderDTO) {
        AssessmentHeader assessmentHeader = new AssessmentHeader();
        assessmentHeader.setInstitution(assessmentHeaderDTO.getInstitution());
        assessmentHeader.setDepartment(assessmentHeaderDTO.getDepartment());
        assessmentHeader.setCourse(assessmentHeaderDTO.getCourse());
        assessmentHeader.setClassroom(assessmentHeaderDTO.getClassroom());
        assessmentHeader.setProfessor(assessmentHeaderDTO.getProfessor());
        assessmentHeader.setInstructions(assessmentHeaderDTO.getInstructions());
        assessmentHeader.setCreationDate(LocalDate.now());
        assessmentHeader.setUserId(assessmentHeaderDTO.getUserId());

        return assessmentHeaderRepository.save(assessmentHeader);
    }

    // BUSCAR 1 HEADER POR ID
    public AssessmentHeader getHeaderById(Long id) {
        return assessmentHeaderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Header not found with ID: " + id));
    }

    // BUSCA TODOS HEADERS DE UM USER
    public List<AssessmentHeaderUserDTO> getAllHeadersByUser(Long userId) {
        List<AssessmentHeader> headers = assessmentHeaderRepository.findAllHeadersByUserId(userId);
        return headers.stream()
                .map(header -> new AssessmentHeaderUserDTO(
                        header.getId(),
                        header.getInstitution(),
                        header.getDepartment(),
                        header.getCourse(),
                        header.getClassroom(),
                        header.getProfessor(),
                        header.getInstructions(),
                        header.getCreationDate().toString(),
                        header.getUserId()))
                .collect(Collectors.toList());
    }

    // // ATUALIZA HEADER POR ID
    // public AssessmentHeader updateHeader(Long id, AssessmentHeaderDTO updatedHeaderDTO) {
    //     AssessmentHeader existingHeader = assessmentHeaderRepository.findById(id)
    //             .orElseThrow(() -> new EntityNotFoundException("Header not found with ID: " + id));

    //     if (updatedHeaderDTO.getInstitution() != null)
    //         existingHeader.setInstitution(updatedHeaderDTO.getInstitution());
    //     if (updatedHeaderDTO.getDepartment() != null)
    //         existingHeader.setDepartment(updatedHeaderDTO.getDepartment());
    //     if (updatedHeaderDTO.getCourse() != null)
    //         existingHeader.setCourse(updatedHeaderDTO.getCourse());
    //     if (updatedHeaderDTO.getClassroom() != null)
    //         existingHeader.setClassroom(updatedHeaderDTO.getClassroom());
    //     if (updatedHeaderDTO.getProfessor() != null)
    //         existingHeader.setProfessor(updatedHeaderDTO.getProfessor());
    //     if (updatedHeaderDTO.getInstructions() != null)
    //         existingHeader.setInstructions(updatedHeaderDTO.getInstructions());

    //     return assessmentHeaderRepository.save(existingHeader);
    // }

    // DELETA HEADER POR ID
    public void deleteHeader(Long id) {
        if (!assessmentHeaderRepository.existsById(id)) {
            throw new EntityNotFoundException("Header not found with ID: " + id);
        }
        assessmentHeaderRepository.deleteById(id);
    }
}

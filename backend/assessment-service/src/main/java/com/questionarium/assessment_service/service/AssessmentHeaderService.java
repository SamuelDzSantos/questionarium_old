package com.questionarium.assessment_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AssessmentHeader;
import com.questionarium.assessment_service.repository.AssessmentHeaderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AssessmentHeaderService {

    private final AssessmentHeaderRepository assessmentHeaderRepository;

    /** Cria um novo header */
    public AssessmentHeader createHeader(AssessmentHeader header) {
        log.info("Criando novo AssessmentHeader para usuário {}", header.getUserId());
        return assessmentHeaderRepository.save(header);
    }

    /** Busca header por ID ou lança 404 */
    @Transactional(readOnly = true)
    public AssessmentHeader getHeaderById(Long id) {
        log.info("Buscando AssessmentHeader com id {}", id);
        return assessmentHeaderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Header de avaliação não encontrado: " + id));
    }

    /** Busca headers por userId */
    @Transactional(readOnly = true)
    public List<AssessmentHeader> getHeadersByUserId(Long userId) {
        log.info("Buscando AssessmentHeaders do usuário {}", userId);
        return assessmentHeaderRepository.findByUserId(userId);
    }

    /** Busca todos os headers */
    @Transactional(readOnly = true)
    public List<AssessmentHeader> getAllHeaders() {
        log.info("Buscando todos os AssessmentHeaders");
        return assessmentHeaderRepository.findAll();
    }

    /** Atualiza um header existente */
    public AssessmentHeader updateHeader(Long id, AssessmentHeader updatedHeader) {
        log.info("Atualizando AssessmentHeader com id {}", id);

        AssessmentHeader existingHeader = assessmentHeaderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Header de avaliação não encontrado para atualização: " + id));

        existingHeader.setInstitution(updatedHeader.getInstitution());
        existingHeader.setDepartment(updatedHeader.getDepartment());
        existingHeader.setCourse(updatedHeader.getCourse());
        existingHeader.setClassroom(updatedHeader.getClassroom());
        existingHeader.setProfessor(updatedHeader.getProfessor());
        existingHeader.setInstructions(updatedHeader.getInstructions());
        existingHeader.setImage(updatedHeader.getImage());

        AssessmentHeader saved = assessmentHeaderRepository.save(existingHeader);
        log.info("AssessmentHeader {} atualizado com sucesso", saved.getId());
        return saved;
    }

    /** Deleta um header */
    public void deleteHeader(Long id) {
        log.info("Deletando AssessmentHeader com id {}", id);

        if (!assessmentHeaderRepository.existsById(id)) {
            throw new BusinessException("Header de avaliação não encontrado para exclusão: " + id);
        }

        assessmentHeaderRepository.deleteById(id);
        log.info("AssessmentHeader {} deletado com sucesso", id);
    }
}

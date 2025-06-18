package com.questionarium.assessment_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionarium.assessment_service.exception.BusinessException;
import com.questionarium.assessment_service.model.AssessmentHeader;
import com.questionarium.assessment_service.repository.AssessmentHeaderRepository;
import com.questionarium.assessment_service.security.JwtTokenDecoder;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AssessmentHeaderService {

    private final AssessmentHeaderRepository assessmentHeaderRepository;
    private final JwtTokenDecoder jwtUtils;   // injete aqui

    /** Cria um novo header, atribuindo o userId do token */
    public AssessmentHeader createHeader(AssessmentHeader header) {
        Long currentUserId = jwtUtils.getCurrentUserId();   // use o JwtUtils
        header.setUserId(currentUserId);
        log.info("Criando novo AssessmentHeader para usuário {}", currentUserId);
        return assessmentHeaderRepository.save(header);
    }

    /** Busca header por ID ou lança 404 */
    @Transactional(readOnly = true)
    public AssessmentHeader getHeaderById(Long id) {
        log.info("Buscando AssessmentHeader com id {}", id);
        AssessmentHeader header = assessmentHeaderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Header de avaliação não encontrado: " + id));
        // valida acesso do usuário
        if (!isAdmin() && !header.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("Você não tem permissão para acessar este header");
        }
        return header;
    }

    /** Busca headers do usuário logado */
    @Transactional(readOnly = true)
    public List<AssessmentHeader> getHeadersByUser() {
        Long currentUserId = getCurrentUserId();
        log.info("Buscando AssessmentHeaders do usuário {}", currentUserId);
        return assessmentHeaderRepository.findByUserId(currentUserId);
    }

    /** Busca todos os headers (somente admin) */
    @Transactional(readOnly = true)
    public List<AssessmentHeader> getAllHeaders() {
        if (!isAdmin()) {
            throw new BusinessException("Somente administradores podem listar todos os headers");
        }
        log.info("ADMIN: buscando todos os AssessmentHeaders");
        return assessmentHeaderRepository.findAll();
    }

    /** Atualiza um header existente */
    public AssessmentHeader updateHeader(Long id, AssessmentHeader updatedHeader) {
        Long currentUserId = getCurrentUserId();
        log.info("Atualizando AssessmentHeader com id {}", id);

        AssessmentHeader existing = assessmentHeaderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Header de avaliação não encontrado para atualização: " + id));

        if (!isAdmin() && !existing.getUserId().equals(currentUserId)) {
            throw new BusinessException("Você não tem permissão para atualizar este header");
        }

        existing.setInstitution(updatedHeader.getInstitution());
        existing.setDepartment(updatedHeader.getDepartment());
        existing.setCourse(updatedHeader.getCourse());
        existing.setClassroom(updatedHeader.getClassroom());
        existing.setProfessor(updatedHeader.getProfessor());
        existing.setInstructions(updatedHeader.getInstructions());
        existing.setImage(updatedHeader.getImage());

        AssessmentHeader saved = assessmentHeaderRepository.save(existing);
        log.info("AssessmentHeader {} atualizado com sucesso", saved.getId());
        return saved;
    }

    /** Deleta um header */
    public void deleteHeader(Long id) {
        Long currentUserId = getCurrentUserId();
        log.info("Deletando AssessmentHeader com id {}", id);

        AssessmentHeader existing = assessmentHeaderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Header de avaliação não encontrado para exclusão: " + id));

        if (!isAdmin() && !existing.getUserId().equals(currentUserId)) {
            throw new BusinessException("Você não tem permissão para excluir este header");
        }

        assessmentHeaderRepository.deleteById(id);
        log.info("AssessmentHeader {} deletado com sucesso", id);
    }

    /** Extrai o ID do usuário do JWT */
    private Long getCurrentUserId() {
        return jwtUtils.getCurrentUserId();
    }

    /** Verifica se o usuário atual tem ROLE_ADMIN */
    private boolean isAdmin() {
        return jwtUtils.isAdmin();
    }
}

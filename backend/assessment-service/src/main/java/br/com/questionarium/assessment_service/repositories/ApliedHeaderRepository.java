package br.com.questionarium.assessment_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.questionarium.assessment_service.models.ApliedHeader;

@Repository
public interface ApliedHeaderRepository extends JpaRepository<ApliedHeader, Long> {

    List<ApliedHeader> findAllHeadersByUserId(Long userId);

}

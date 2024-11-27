package br.com.questionarium.assessment_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.questionarium.assessment_service.models.AppliedHeader;

@Repository
public interface AppliedHeaderRepository extends JpaRepository<AppliedHeader, Long> {

    List<AppliedHeader> findAllHeadersByUserId(Long userId);

}

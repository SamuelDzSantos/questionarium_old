package br.com.questionarium.question_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.questionarium.question_service.model.Alternative;

public interface AlternativeRepository extends JpaRepository<Alternative, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM question_alternative WHERE question_id = :questionId", nativeQuery = true)
    void deleteAllByQuestionId(@Param("questionId") Long questionId);

    @Query(value = "SELECT * FROM question_alternative WHERE question_id = :questionId", nativeQuery = true)
    List<Alternative> findAllByQuestionId(@Param("questionId") Long questionId);
}

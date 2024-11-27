package br.com.questionarium.question_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.questionarium.question_service.model.Header;

public interface HeaderRepository extends JpaRepository<Header, Long>{

    
}
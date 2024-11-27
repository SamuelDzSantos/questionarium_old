package br.com.questionarium.question_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.questionarium.question_service.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>{
    
}

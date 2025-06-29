package com.github.questionarium.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.questionarium.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}

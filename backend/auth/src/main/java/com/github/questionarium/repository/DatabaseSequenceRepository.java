package com.github.questionarium.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.questionarium.model.DatabaseSequence;

@Repository
public interface DatabaseSequenceRepository extends MongoRepository<DatabaseSequence, String> {

}

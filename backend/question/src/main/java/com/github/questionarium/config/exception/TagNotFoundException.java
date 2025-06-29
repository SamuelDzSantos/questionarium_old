package com.github.questionarium.config.exception;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long id) {
        super("Tag not found with ID " + id);
    }
}
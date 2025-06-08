package br.com.questionarium.question_service.exception;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long id) {
        super("Tag not found with ID " + id);
    }
}
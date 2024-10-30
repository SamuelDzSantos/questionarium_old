package org.ufpr.questionarium.exceptions;

import static org.ufpr.questionarium.types.ErrorType.INVALID_INPUT;
import static org.ufpr.questionarium.types.ErrorType.NOT_FOUND;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.ufpr.questionarium.exceptions.exceptions.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDetails> handleNoSuchElementException(NoSuchElementException e) {

        ErrorDetails details = new ErrorDetails(new Date(), NOT_FOUND.toString(), e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(details);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException e) {

        ErrorDetails details = new ErrorDetails(new Date(), "", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailsMap> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });

        ErrorDetailsMap details = new ErrorDetailsMap(new Date(), INVALID_INPUT.toString(), errors);

        return ResponseEntity.badRequest().body(details);

    }

}

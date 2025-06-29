package com.github.questionarium.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApiErrorException extends ResponseStatusException {

    public ApiErrorException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
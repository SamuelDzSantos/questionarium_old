package com.questionarium.assessment_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Erros de regra de negócio (HTTP 400 Bad Request).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    /** Código interno opcional para tratamento pelo cliente */
    private final String errorCode;

    public BusinessException(String message) {
        this(message, null, null);
    }

    public BusinessException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    public BusinessException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
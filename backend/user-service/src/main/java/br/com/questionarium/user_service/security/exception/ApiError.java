package br.com.questionarium.user_service.security.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApiError {
    private LocalDateTime timestamp;
    private String error;
    private String message;
    private String path;

    public ApiError(
            LocalDateTime timestamp,
            String error,
            String message,
            String path) {
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}

package com.github.questionarium.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(-2)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotConfirmedException.class)
    public ResponseEntity<Object> handleUserNotConfirmed(UserNotConfirmedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getReason(), null);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Usuário não encontrado.", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.", ex.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Object> handleRestClientException(RestClientException ex) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "Falha na comunicação com UserService.", ex.getMessage());
    }

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<Object> handleApiError(ApiErrorException ex) {
        // Converte HttpStatusCode para HttpStatus
        HttpStatusCode sc = ex.getStatusCode();
        HttpStatus status = HttpStatus.resolve(sc.value());
        return buildResponse(status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR, ex.getReason(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no AuthService.", ex.getMessage());
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String error, String details) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        if (details != null)
            body.put("details", details);

        log.error("[GlobalExceptionHandler] {} - {}", error, details);
        return new ResponseEntity<>(body, status);
    }
}
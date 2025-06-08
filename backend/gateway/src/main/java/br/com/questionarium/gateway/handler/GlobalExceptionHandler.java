package br.com.questionarium.gateway.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(-2) // Alta prioridade para interceptar primeiro
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Token JWT inválido ou expirado.", ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = (HttpStatus) ex.getStatusCode(); // CAST necessário aqui
        return buildResponse(status, ex.getReason(), ex.getMessage());
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<Object> handleServerWebInputException(ServerWebInputException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Requisição inválida.", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no Gateway.", ex.getMessage());
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String error, String details) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("details", details);

        System.out.println("[Gateway] EXCEPTION HANDLER: " + error + " | " + details);

        return new ResponseEntity<>(body, status);
    }
}

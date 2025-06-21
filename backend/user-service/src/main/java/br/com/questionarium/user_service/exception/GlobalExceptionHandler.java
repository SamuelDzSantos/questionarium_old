package br.com.questionarium.user_service.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

/**
 * Trata exceções personalizadas lançadas pelo User Service.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // cria ApiError com path opcional
    private ResponseEntity<ApiError> buildResponse(HttpStatus status,
            String error,
            String message,
            String path) {
        ApiError apiError = new ApiError(
                status.value(),
                error,
                message,
                LocalDateTime.now(),
                path);
        return ResponseEntity.status(status).body(apiError);
    }

    // wrapper sem path
    private ResponseEntity<ApiError> buildResponse(HttpStatus status,
            String error,
            String message) {
        return buildResponse(status, error, message, null);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT,
                "Email já cadastrado",
                ex.getMessage());
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ApiError> handleTokenInvalid(TokenInvalidException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Token inválido ou expirado",
                ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND,
                "Usuário não encontrado",
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());
        String message = String.join("; ", errors);
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Erro de validação",
                message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        logger.error("Violação de integridade de dados", ex);
        return buildResponse(HttpStatus.CONFLICT,
                "Violação de integridade",
                ex.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex,
            HttpServletRequest req) {
        logger.warn("Acesso negado {} {}: {}",
                req.getMethod(), req.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN,
                "Acesso negado",
                ex.getMessage(),
                req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(Exception ex) {
        logger.error("Erro interno inesperado", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro inesperado.");
    }
}

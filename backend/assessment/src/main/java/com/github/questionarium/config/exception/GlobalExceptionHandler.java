package com.github.questionarium.config.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // 400 para JSON malformado
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Object> handleMalformedJson(HttpMessageNotReadableException ex) {
                return buildResponse(HttpStatus.BAD_REQUEST,
                                "Formato de JSON inválido",
                                ex.getLocalizedMessage());
        }

        // 400 para BusinessException (regras de negócio)
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<Object> handleBusiness(BusinessException ex) {
                // Se implementou errorCode na BusinessException, inclua no detalhes:
                Map<String, Object> detalhes = new LinkedHashMap<>();
                detalhes.put("message", ex.getMessage());
                if (ex.getErrorCode() != null) {
                        detalhes.put("errorCode", ex.getErrorCode());
                }
                return buildResponse(HttpStatus.BAD_REQUEST,
                                "Regra de negócio violada",
                                detalhes);
        }

        // 400 para @Valid em @RequestBody
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
                Map<String, String> fieldErrors = ex.getBindingResult()
                                .getFieldErrors().stream()
                                .collect(Collectors.toMap(
                                                err -> err.getField(),
                                                err -> err.getDefaultMessage()));
                return buildResponse(HttpStatus.BAD_REQUEST,
                                "Erro de validação nos campos",
                                fieldErrors);
        }

        // 400 para @RequestParam, @PathVariable inválidos
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
                List<String> details = ex.getConstraintViolations().stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.toList());
                return buildResponse(HttpStatus.BAD_REQUEST,
                                "Violação de restrição",
                                details);
        }

        // 404 para entidades não encontradas
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
                return buildResponse(HttpStatus.NOT_FOUND,
                                "Não encontrado",
                                ex.getMessage());
        }

        /*
         * // 500 para qualquer outro erro
         * 
         * @ExceptionHandler(Exception.class)
         * public ResponseEntity<Object> handleGeneral(Exception ex) {
         * 
         * System.out.println(ex.getStackTrace());
         * System.out.println(ex.getSuppressed());
         * return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
         * "Erro interno inesperado",
         * ex.getMessage());
         * }
         */
        private ResponseEntity<Object> buildResponse(HttpStatus status,
                        String error,
                        Object details) {
                LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();
                body.put("status", status.value());
                body.put("erro", error);
                body.put("detalhes", details);
                return ResponseEntity.status(status).body(body);
        }
}

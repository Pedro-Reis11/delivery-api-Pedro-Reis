package com.delivery_api.Projeto.Delivery.API.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =============================
    // 404 - Entidade não encontrada
    // =============================
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ValidationErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Entidade não encontrada",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // =============================
    // 400 - Regras de Negócio
    // =============================
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ValidationErrorResponse> handleBusinessException(BusinessException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de regra de negócio",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // =============================
    // 400 - Validações @Valid
    // =============================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError) err).getField();
            String msg = err.getDefaultMessage();
            errors.put(field, msg);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Dados inválidos",
                errors.toString(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // =============================
    // 401 - Token expirado
    // =============================
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ValidationErrorResponse> handleExpiredJWT(ExpiredJwtException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Token expirado",
                "Realize login novamente",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // =============================
    // 403 - Acesso negado
    // =============================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ValidationErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado",
                "Você não possui permissão para acessar este recurso",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // =============================
    // 404 - Usuário não encontrado
    // =============================
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ValidationErrorResponse> handleUserNotFound(UsernameNotFoundException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Usuário não encontrado",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // =============================
    // 401 - JWT inválido
    // =============================
    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<ValidationErrorResponse> handleJwtException(io.jsonwebtoken.JwtException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Token inválido",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // =============================
    // 500 - Erro interno genérico
    // =============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ValidationErrorResponse> handleGenericException(Exception ex) {
        ValidationErrorResponse error = new ValidationErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

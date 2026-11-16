package com.chatlocal.chatlocal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Format d'erreur commun, conforme à docs/API.md :
 * { "status": 400, "erreur": "message", "champs": { ... } }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApi(ApiException e) {
        Map<String, Object> corps = new LinkedHashMap<>();
        corps.put("status", e.getStatus().value());
        corps.put("erreur", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(corps);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> champs = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(f -> champs.put(f.getField(), f.getDefaultMessage()));

        Map<String, Object> corps = new LinkedHashMap<>();
        corps.put("status", HttpStatus.BAD_REQUEST.value());
        corps.put("erreur", "Données invalides");
        corps.put("champs", champs);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corps);
    }
}
package com.rbb.bookshelf.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", 400,
                "error", "BAD_REQUEST",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> conflict(DataIntegrityViolationException ex) {
        return ResponseEntity.status(409).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", 409,
                "error", "CONFLICT",
                "message", "Data integrity violation"
        ));
    }
}

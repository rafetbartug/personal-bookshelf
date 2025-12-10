package com.rbb.bookshelf.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ApiError(Instant timestamp, int status, String error, String message, String path, Map<String,String> messages) {}

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException ex, jakarta.servlet.http.HttpServletRequest req) {
        return new ApiError(Instant.now(), 404, "NOT_FOUND", ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(IllegalArgumentException ex, jakarta.servlet.http.HttpServletRequest req) {
        return new ApiError(Instant.now(), 400, "BAD_REQUEST", ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException ex, jakarta.servlet.http.HttpServletRequest req) {
        Map<String,String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));
        return new ApiError(Instant.now(), 400, "BAD_REQUEST", "Validation failed", req.getRequestURI(), fieldErrors);
    }
}

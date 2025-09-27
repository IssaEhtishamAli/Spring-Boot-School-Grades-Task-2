package com.school.management.Handler;

import com.school.management.Models.Generic.mGeneric;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public mGeneric.mAPIResponse<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new mGeneric.mAPIResponse<>(400, "Validation failed: " + errors);
    }

    // Generic errors
    @ExceptionHandler(Exception.class)
    public mGeneric.mAPIResponse<Object> handleGenericException(Exception ex) {
        return new mGeneric.mAPIResponse<>(500, "Internal server error: " + ex.getMessage());
    }
}

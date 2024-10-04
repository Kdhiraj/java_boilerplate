package com.eventline.shared.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AbstractApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(AbstractApiException ex) {
        logger.warn("API Error: {}", ex.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                Collections.singletonList(new ApiErrorResponse.FieldError(ex.getField(), ex.getMessage()))
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Validation Error: {}", ex.getMessage());
        List<ApiErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiErrorResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                ex.getStatusCode().value(),
                "Validation Error",
                fieldErrors
        );

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

}

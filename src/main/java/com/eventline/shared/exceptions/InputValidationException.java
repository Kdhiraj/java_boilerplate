package com.eventline.shared.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for handling validation errors (HTTP 422).
 */
public class InputValidationException extends AbstractApiException {
    public InputValidationException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    public InputValidationException(String message, String field) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, message, field);
    }

    public InputValidationException(String message, Throwable cause, String field) {
        super(message, cause, HttpStatus.UNPROCESSABLE_ENTITY, field);
    }
}

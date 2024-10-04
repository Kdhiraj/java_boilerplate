package com.eventline.shared.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for handling bad requests (HTTP 400).
 */
public class BadInputException extends AbstractApiException {
    public BadInputException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public BadInputException(String message, String field) {
        super(HttpStatus.BAD_REQUEST, message, field);
    }

    public BadInputException(String message, Throwable cause, String field) {
        super(message, cause, HttpStatus.BAD_REQUEST, field);
    }
}

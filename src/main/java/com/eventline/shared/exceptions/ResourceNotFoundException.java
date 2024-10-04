package com.eventline.shared.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for handling resource not found errors (HTTP 404).
 */
public class ResourceNotFoundException extends AbstractApiException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND);
    }
}

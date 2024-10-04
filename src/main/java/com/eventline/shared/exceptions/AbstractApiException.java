package com.eventline.shared.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for all custom application exceptions.
 * Provides HTTP status, error message, and optional field details for validation errors.
 */
@Getter
public abstract class AbstractApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String field;

    /**
     * Constructor for exceptions with a message and a specific field.
     */
    protected AbstractApiException(HttpStatus httpStatus, String message, String field) {
        super(message);
        this.httpStatus = httpStatus;
        this.field = field;
    }

    /**
     * Constructor for exceptions with a message but without a specific field.
     */
    protected AbstractApiException(HttpStatus httpStatus, String message) {
        this(httpStatus, message, null);
    }

    /**
     * Constructor for exceptions with a cause (stack trace) and a specific field.
     */
    protected AbstractApiException(String message, Throwable cause, HttpStatus httpStatus, String field) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.field = field;
    }

    /**
     * Constructor for exceptions with a cause (stack trace) but without a specific field.
     */
    protected AbstractApiException(String message, Throwable cause, HttpStatus httpStatus) {
        this(message, cause, httpStatus, null);
    }
}

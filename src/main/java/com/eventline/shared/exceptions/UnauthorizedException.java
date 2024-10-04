package com.eventline.shared.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractApiException {

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }


    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNAUTHORIZED);
    }
}

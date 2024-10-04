package com.eventline.shared.exceptions;

import org.springframework.http.HttpStatus;

public class TooEarlyRequestException extends AbstractApiException {

    public TooEarlyRequestException(String message) {
        super(HttpStatus.TOO_EARLY, message);
    }


    public TooEarlyRequestException(String message, Throwable cause) {
        super(message, cause, HttpStatus.TOO_EARLY);
    }
}

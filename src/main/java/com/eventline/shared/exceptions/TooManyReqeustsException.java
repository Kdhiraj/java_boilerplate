package com.eventline.shared.exceptions;

import org.springframework.http.HttpStatus;

public class TooManyReqeustsException extends AbstractApiException {

    public TooManyReqeustsException(String message) {
        super(HttpStatus.TOO_MANY_REQUESTS, message);
    }


    public TooManyReqeustsException(String message, Throwable cause) {
        super(message, cause, HttpStatus.TOO_MANY_REQUESTS);
    }
}

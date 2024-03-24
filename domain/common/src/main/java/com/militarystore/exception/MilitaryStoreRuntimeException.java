package com.militarystore.exception;

import org.springframework.http.HttpStatus;

public abstract class MilitaryStoreRuntimeException extends RuntimeException {

    public final HttpStatus statusCode;

    protected MilitaryStoreRuntimeException(String message, Throwable cause, HttpStatus statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    protected MilitaryStoreRuntimeException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}

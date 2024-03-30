package com.militarystore.exception;

import org.springframework.http.HttpStatus;

public class MsValidationException extends MilitaryStoreRuntimeException {

    public MsValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

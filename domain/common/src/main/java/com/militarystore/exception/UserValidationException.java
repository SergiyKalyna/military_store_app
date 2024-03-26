package com.militarystore.exception;

import org.springframework.http.HttpStatus;

public class UserValidationException extends MilitaryStoreRuntimeException {

    public UserValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

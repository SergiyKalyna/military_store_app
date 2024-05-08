package com.militarystore.exception;

import org.springframework.http.HttpStatus;

public class WrongPasswordException extends MilitaryStoreRuntimeException {

    public WrongPasswordException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

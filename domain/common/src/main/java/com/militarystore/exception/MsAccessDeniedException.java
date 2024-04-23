package com.militarystore.exception;

import org.springframework.http.HttpStatus;

public class MsAccessDeniedException extends MilitaryStoreRuntimeException {

    public MsAccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

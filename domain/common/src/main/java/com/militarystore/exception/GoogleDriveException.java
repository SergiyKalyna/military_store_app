package com.militarystore.exception;

import org.springframework.http.HttpStatus;

public class GoogleDriveException extends MilitaryStoreRuntimeException {

    public GoogleDriveException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.militarystore.exception;

import org.springframework.http.HttpStatus;

public class MsNotFoundException extends MilitaryStoreRuntimeException {

    public MsNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

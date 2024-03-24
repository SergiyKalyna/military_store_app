package com.militarystore.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MilitaryStoreRuntimeException {
    private static final String ERROR_MESSAGE = "User with id [%d] is not found";

    public UserNotFoundException(int userId) {
        super(String.format(ERROR_MESSAGE, userId), HttpStatus.NOT_FOUND);
    }
}

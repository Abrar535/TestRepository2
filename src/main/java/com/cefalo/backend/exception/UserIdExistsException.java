package com.cefalo.backend.exception;

public class UserIdExistsException extends Exception {
    public UserIdExistsException(String errorMessage) {
        super(errorMessage);
    }
}

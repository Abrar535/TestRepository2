package com.example.learnspring.model.exception;

public class UserIdExistsException extends Exception {
    public UserIdExistsException(String errorMessage) {
        super(errorMessage);
    }
}

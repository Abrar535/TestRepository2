package com.example.learnspring.exception;

public class UserIdExistsException extends Exception {
    public UserIdExistsException(String errorMessage) {
        super(errorMessage);
    }
}

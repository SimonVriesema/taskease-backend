package com.taskease.taskeasebackend.exceptions;

public class UserAlreadyInProjectException extends RuntimeException {
    public UserAlreadyInProjectException(String message) {
        super(message);
    }
}

package com.taskease.taskeasebackend.exceptions;

public class InvalidInputDataException extends RuntimeException {
    public InvalidInputDataException(String message) {
        super(message);
    }
}

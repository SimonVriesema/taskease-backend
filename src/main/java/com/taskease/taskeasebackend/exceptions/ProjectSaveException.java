package com.taskease.taskeasebackend.exceptions;

public class ProjectSaveException extends RuntimeException {
    public ProjectSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
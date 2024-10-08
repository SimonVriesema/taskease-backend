package com.taskease.taskeasebackend.exceptions;

public class ProjectLeaderNotFoundException extends RuntimeException {
    public ProjectLeaderNotFoundException(String message) {
        super(message);
    }
}

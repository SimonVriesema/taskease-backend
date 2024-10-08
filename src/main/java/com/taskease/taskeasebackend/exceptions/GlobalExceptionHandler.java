package com.taskease.taskeasebackend.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    protected ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                "Project not found"
        );
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    protected ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                "Task not found"
        );
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                "User not found"
        );
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ProjectSaveException.class)
    protected ResponseEntity<Object> handleProjectSaveException(ProjectSaveException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                "Failed to save project"
        );
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(UserAlreadyInProjectException.class)
    protected ResponseEntity<Object> handleUserAlreadyInProjectException(UserAlreadyInProjectException ex,
                                                                         WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                "User is already in the project"
        );
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ProjectLeaderNotFoundException.class)
    protected ResponseEntity<Object> handleProjectLeaderNotFoundException(ProjectLeaderNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                "Project leader not found"
        );
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidInputDataException.class)
    protected ResponseEntity<Object> handleInvalidInputDataException(InvalidInputDataException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDate.now(),
                ex.getMessage(),
                "Invalid input data"
        );
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}

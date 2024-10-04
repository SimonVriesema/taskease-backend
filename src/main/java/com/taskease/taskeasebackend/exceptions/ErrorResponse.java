package com.taskease.taskeasebackend.exceptions;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ErrorResponse {
    @NotNull
    private LocalDate localDate;
    @NotNull
    private String message;
    @NotNull
    private String details;
}

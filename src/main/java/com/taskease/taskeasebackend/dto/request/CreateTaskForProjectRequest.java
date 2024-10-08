package com.taskease.taskeasebackend.dto.request;

import com.taskease.taskeasebackend.enums.Priority;
import com.taskease.taskeasebackend.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskForProjectRequest {
    @NotBlank(message = "Task title cannot be null or empty")
    @Size(max = 50)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Task status cannot be null")
    private Status status;

    @NotNull(message = "Task priority cannot be null")
    private Priority priority;

    private LocalDate dueDate;

    @NotNull(message = "Project ID cannot be null")
    private Long projectId;

    private Long assignedUserId;
}
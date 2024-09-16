package com.taskease.taskeasebackend.dto;

import com.taskease.taskeasebackend.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotBlank(message = "Project title cannot be null or empty")
    @Size(max = 50)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Project leader ID cannot be null")
    private Long projectLeaderId;

    @NotNull(message = "Project status cannot be null")
    private Status status;
}
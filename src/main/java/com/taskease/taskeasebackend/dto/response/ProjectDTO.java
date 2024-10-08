package com.taskease.taskeasebackend.dto.response;

import com.taskease.taskeasebackend.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate createdDate;
    private Status status;
    private UserDTO projectLeader;
    private List<UserDTO> users;
}

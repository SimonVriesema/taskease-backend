package com.taskease.taskeasebackend.dto.response;

import com.taskease.taskeasebackend.enums.Priority;
import com.taskease.taskeasebackend.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private LocalDate createdDate;
    private ProjectInfo projectInfo;
    private UserInfo userInfo;

    @Data
    public static class ProjectInfo {
        private Long projectId;
        private String projectTitle;
    }

    @Data
    public static class UserInfo {
        private Long assignedUserId;
        private String assignedUserName;
    }
}

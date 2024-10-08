package com.taskease.taskeasebackend.utils;

import com.taskease.taskeasebackend.dto.response.ProjectDTO;
import com.taskease.taskeasebackend.dto.response.TaskDTO;
import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.models.User;

import java.util.stream.Collectors;

public class DTOConvertor {
    public static ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setTitle(project.getTitle());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setCreatedDate(project.getCreatedDate());
        projectDTO.setStatus(project.getStatus());
        projectDTO.setProjectLeader(convertToDTO(project.getProjectLeader()));
        projectDTO.setUsers(project.getUsers().stream().map(DTOConvertor::convertToDTO).collect(Collectors.toList()));
        return projectDTO;
    }

    public static UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

    public static TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setCreatedDate(task.getCreatedDate());

        TaskDTO.ProjectInfo projectInfo = new TaskDTO.ProjectInfo();
        if (task.getProject() != null) {
            projectInfo.setProjectId(task.getProject().getId());
            projectInfo.setProjectTitle(task.getProject().getTitle());
        }
        taskDTO.setProjectInfo(projectInfo);

        TaskDTO.UserInfo userInfo = new TaskDTO.UserInfo();
        if (task.getAssignedUser() != null) {
            userInfo.setAssignedUserId(task.getAssignedUser().getId());
            userInfo.setAssignedUserName(task.getAssignedUser().getUsername());
        }
        taskDTO.setUserInfo(userInfo);

        return taskDTO;
    }
}

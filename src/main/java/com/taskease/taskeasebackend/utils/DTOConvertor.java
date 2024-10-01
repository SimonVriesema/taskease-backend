package com.taskease.taskeasebackend.utils;

import com.taskease.taskeasebackend.dto.response.ProjectDTO;
import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.models.Project;
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
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        return userDTO;
    }
}

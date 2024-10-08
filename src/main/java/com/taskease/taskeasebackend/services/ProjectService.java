package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.dto.request.CreateProjectRequest;
import com.taskease.taskeasebackend.dto.response.ProjectDTO;
import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.enums.Status;
import com.taskease.taskeasebackend.exceptions.*;
import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.ProjectRepository;
import com.taskease.taskeasebackend.repositories.UserRepository;
import com.taskease.taskeasebackend.utils.DTOConvertor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project saveProject(Project project) {
        if (project.getCreatedDate() == null) {
            project.setCreatedDate(LocalDate.from(LocalDateTime.now()));
        }

        project.setProjectLeader(validateUser(project.getProjectLeader().getId()));

        try {
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectSaveException("Failed to save project", e);
        }
    }

    public void deleteProjectById(Long projectId) {
        validateProjectExists(projectId);
        projectRepository.deleteById(projectId);
    }

    public boolean doesProjectExist(Long projectId) {
        validateProjectId(projectId);
        return projectRepository.existsById(projectId);
    }

    public Project findById(Long projectId) {
        validateProjectId(projectId);
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(String.format("Project with ID %d not found", projectId)));
    }

    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new ProjectNotFoundException("No projects found");
        }
        return projects;
    }

    public List<Project> getProjectsByUserId(Long userId) {
        List<Project> projects = projectRepository.findByProjectLeader_Id(userId);
        if (projects.isEmpty()) {
            throw new ProjectNotFoundException(String.format("No projects found for user ID %d", userId));
        }
        projects.forEach(project -> project.getUsers().size());
        return projects;
    }

    public Project updateProject(Long id, Project project) {
        validateProjectExists(id);
        project.setId(id);
        return projectRepository.save(project);
    }

    @Transactional
    public Project removeUserFromProjectInternal(Long projectId, Long userId) {
        Project project = validateProjectExists(projectId);
        User user = validateUser(userId);

        if (!project.getUsers().contains(user)) {
            throw new UserNotFoundException("User is not part of the project");
        }

        project.getUsers().remove(user);
        user.getProjects().remove(project);

        projectRepository.save(project);
        userRepository.save(user);

        return project;
    }

    @Transactional
    public Project addUserToProjectInternal(Long projectId, Long userId) {
        Project project = validateProjectExists(projectId);
        User user = validateUser(userId);

        if (project.getUsers().contains(user)) {
            throw new UserAlreadyInProjectException("User is already in the project");
        }

        project.getUsers().add(user);
        user.getProjects().add(project);

        projectRepository.save(project);
        userRepository.save(user);

        return project;
    }

    public List<ProjectDTO> getAllProjectDTOs() {
        return getAllProjects().stream().map(DTOConvertor::convertToDTO).collect(Collectors.toList());
    }

    public ProjectDTO getProjectDTOById(Long id) {
        return DTOConvertor.convertToDTO(findById(id));
    }

    public ProjectDTO createProject(CreateProjectRequest createProjectRequest) {
        User projectLeader = validateUser(createProjectRequest.getProjectLeaderId());

        Project project = new Project();
        project.setTitle(createProjectRequest.getTitle());
        project.setDescription(createProjectRequest.getDescription());
        project.setProjectLeader(projectLeader);
        project.setStatus(createProjectRequest.getStatus());
        project.getUsers().add(projectLeader);

        return DTOConvertor.convertToDTO(saveProject(project));
    }

    public Status getProjectStatus(Long projectId) {
        return findById(projectId).getStatus();
    }

    public List<ProjectDTO> getProjectDTOsByUserId(Long userId) {
        return getProjectsByUserId(userId).stream().map(DTOConvertor::convertToDTO).collect(Collectors.toList());
    }

    public ProjectDTO addUserToProject(Long projectId, Long userId) {
        return DTOConvertor.convertToDTO(addUserToProjectInternal(projectId, userId));
    }

    public ProjectDTO removeUserFromProject(Long projectId, Long userId) {
        return DTOConvertor.convertToDTO(removeUserFromProjectInternal(projectId, userId));
    }

    public List<UserDTO> getUsersByProjectId(Long projectId) {
        return findById(projectId).getUsers().stream().map(DTOConvertor::convertToDTO).collect(Collectors.toList());
    }

    private void validateProjectId(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
    }

    private Project validateProjectExists(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(String.format("Project with ID %d not found", projectId)));
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %d not found", userId)));
    }
}
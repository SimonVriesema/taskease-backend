package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.exceptions.ProjectNotFoundException;
import com.taskease.taskeasebackend.exceptions.ProjectSaveException;
import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.ProjectRepository;
import com.taskease.taskeasebackend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

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

        if (project.getProjectLeader() != null) {
            Optional<User> userOpt = userRepository.findById(project.getProjectLeader().getId());
            userOpt.ifPresent(project::setProjectLeader);
        }

        try {
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectSaveException("Failed to save project", e);
        }
    }

    public void deleteProjectById(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(String.format("Project with ID %d not found", projectId));
        }
        projectRepository.deleteById(projectId);
    }

    public boolean doesProjectExist(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        return projectRepository.existsById(projectId);
    }

    public Project findById(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(String.format("Project with ID %d not found", projectId)));
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
        if (projectRepository.existsById(id)) {
            project.setId(id);
            return projectRepository.save(project);
        } else {
            return null;
        }
    }

    public List<UserDTO> getUsersByProjectId(Long projectId) {
        Project project = findById(projectId);
        List<User> users = project.getUsers();

        logger.info("Found {} users for project ID {}", users.size(), projectId);

        return users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setUsername(user.getUsername());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Project addUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!project.getUsers().contains(user)) {
            project.getUsers().add(user);
            user.getProjects().add(project);

            projectRepository.save(project);
            userRepository.save(user);
        }

        return project;
    }
}

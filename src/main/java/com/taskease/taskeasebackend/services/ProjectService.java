package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.exceptions.ProjectNotFoundException;
import com.taskease.taskeasebackend.exceptions.ProjectSaveException;
import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.ProjectRepository;
import com.taskease.taskeasebackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}

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
        if (project.getTitle() == null || project.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Project title cannot be null or empty");
        }

        if (project.getCreatedDate() == null) {
            project.setCreatedDate(LocalDate.from(LocalDateTime.now()));
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

    public boolean findById(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        return projectRepository.existsById(projectId);
    }

//    public List<Project> getProjectsByUserId(Long userId) {
//        return projectRepository.findByUsers_IdOrProjectLeader_Id(userId, userId);
//    }

//    public Project addUserToProject(Long projectId, Long userId) {
//        Optional<Project> projectOpt = projectRepository.findById(projectId);
//        if (projectOpt.isEmpty()) {
//            throw new ProjectNotFoundException(String.format("Project with ID %d not found", projectId));
//        }
//
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (userOpt.isEmpty()) {
//            throw new IllegalArgumentException(String.format("User with ID %d not found", userId));
//        }
//
//        Project project = projectOpt.get();
//        User user = userOpt.get();
//        project.getUsers().add(user);
//
//        try {
//            return projectRepository.save(project);
//        } catch (Exception e) {
//            throw new ProjectSaveException("Failed to save project", e);
//        }
//    }
}

package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.dto.request.CreateTaskForProjectRequest;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.ProjectRepository;
import com.taskease.taskeasebackend.repositories.TaskRepository;
import com.taskease.taskeasebackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Task createTask(Task task) {
        if (task.getAssignedUser() != null && task.getAssignedUser().getId() != null) {
            User user = userRepository.findById(task.getAssignedUser().getId()).orElse(null);
            task.setAssignedUser(user);
        }
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        if (task.getAssignedUser() != null && task.getAssignedUser().getId() != null) {
            User user = userRepository.findById(task.getAssignedUser().getId()).orElse(null);
            task.setAssignedUser(user);
        }
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByAssignedUser(User assignedUser) {return taskRepository.findByAssignedUser(assignedUser);}

    public Task createTaskForProject(Long projectId, CreateTaskForProjectRequest request) throws Exception {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            throw new Exception("Project not found");
        }
        Project project = projectOptional.get();
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setProject(project);
        if (request.getAssignedUserId() != null) {
            Optional<User> assignedUserOptional = userRepository.findById(request.getAssignedUserId());
            if (assignedUserOptional.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }
            task.setAssignedUser(assignedUserOptional.get());
        }
        return taskRepository.save(task);
    }
}

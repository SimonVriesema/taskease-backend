package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.dto.request.CreateTaskForProjectRequest;
import com.taskease.taskeasebackend.dto.response.TaskDTO;
import com.taskease.taskeasebackend.exceptions.InvalidInputDataException;
import com.taskease.taskeasebackend.exceptions.ProjectNotFoundException;
import com.taskease.taskeasebackend.exceptions.TaskNotFoundException;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.Project;
import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.ProjectRepository;
import com.taskease.taskeasebackend.repositories.TaskRepository;
import com.taskease.taskeasebackend.repositories.UserRepository;
import com.taskease.taskeasebackend.utils.DTOConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
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

    public TaskDTO createTaskForProject(Long projectId, CreateTaskForProjectRequest createTaskRequest) throws InvalidInputDataException, UserNotFoundException, ProjectNotFoundException {
        validateCreateTaskRequest(createTaskRequest);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        Task task = buildTaskFromRequest(createTaskRequest, project);

        return DTOConvertor.convertToDTO(task);
    }

    private void validateCreateTaskRequest(CreateTaskForProjectRequest createTaskRequest) throws InvalidInputDataException {
        if (createTaskRequest == null || createTaskRequest.getTitle() == null || createTaskRequest.getTitle().isEmpty()) {
            throw new InvalidInputDataException("Task title is required");
        }
    }

    private Task buildTaskFromRequest(CreateTaskForProjectRequest createTaskRequest, Project project) throws UserNotFoundException {
        Task task = new Task();
        task.setTitle(createTaskRequest.getTitle());
        task.setDescription(createTaskRequest.getDescription());
        task.setStatus(createTaskRequest.getStatus());
        task.setPriority(createTaskRequest.getPriority());
        task.setDueDate(createTaskRequest.getDueDate());
        task.setProject(project);

        if (createTaskRequest.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(createTaskRequest.getAssignedUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            task.setAssignedUser(assignedUser);
        }

        return task;
    }

    public TaskDTO updateTask(Long id, Task task) throws InvalidInputDataException, TaskNotFoundException {
        if (task == null || task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new InvalidInputDataException("Task title is required");
        }
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found");
        }
        task.setId(id);
        Task updatedTask = taskRepository.save(task);
        return DTOConvertor.convertToDTO(updatedTask);
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    public List<TaskDTO> getTasksByUserId(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Task> tasks = taskRepository.findByAssignedUser(user);
        return tasks.stream().map(DTOConvertor::convertToDTO).collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksFromProject(Long projectId) throws ProjectNotFoundException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        return project.getTasks().stream()
                .map(DTOConvertor::convertToDTO)
                .collect(Collectors.toList());
    }
}
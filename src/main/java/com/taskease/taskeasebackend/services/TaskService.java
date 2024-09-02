package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.enums.Priority;
import com.taskease.taskeasebackend.enums.Status;
import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.TaskRepository;
import com.taskease.taskeasebackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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

    public List<Task> getTasksByStatus(Status status) {return taskRepository.findByStatus(status);}
    public List<Task> getTasksByPriority(Priority priority) {return taskRepository.findByPriority(priority);}
    public List<Task> getTasksByDueDate(LocalDate dueDate) {return taskRepository.findByDueDate(dueDate);}
    public List<Task> getTasksByAssignedUser(User assignedUser) {return taskRepository.findByAssignedUser(assignedUser);}
    public List<Task> getTasksByTitleContaining(String keyword) {return taskRepository.findByTitleContaining(keyword);}

}

package com.taskease.taskeasebackend.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        User user = new User();
        user.setId(1L);
        task.setAssignedUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        assertEquals(user, createdTask.getAssignedUser());
    }

    @Test
    public void testCreateTaskForProject() throws InvalidInputDataException, UserNotFoundException, ProjectNotFoundException {
        CreateTaskForProjectRequest request = new CreateTaskForProjectRequest();
        request.setTitle("New Task");
        request.setAssignedUserId(1L);

        Project project = new Project();
        project.setId(1L);

        User user = new User();
        user.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(new Task());

        TaskDTO taskDTO = taskService.createTaskForProject(1L, request);

        assertNotNull(taskDTO);
    }

    @Test
    public void testUpdateTask() throws InvalidInputDataException, TaskNotFoundException {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Updated Task");

        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(task)).thenReturn(task);

        TaskDTO updatedTaskDTO = taskService.updateTask(1L, task);

        assertNotNull(updatedTaskDTO);
        assertEquals("Updated Task", updatedTaskDTO.getTitle());
    }

    @Test
    public void testDeleteTaskById() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTaskById(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetTasksByUserId() throws UserNotFoundException {
        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setAssignedUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findByAssignedUser(user)).thenReturn(Collections.singletonList(task));

        List<TaskDTO> tasks = taskService.getTasksByUserId(1L);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
    }

    @Test
    public void testGetTasksFromProject() throws ProjectNotFoundException {
        Project project = mock(Project.class);
        Task task = new Task();
        task.setProject(project);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(project.getTasks()).thenReturn(Collections.singletonList(task));

        List<TaskDTO> tasks = taskService.getTasksFromProject(1L);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
    }
}
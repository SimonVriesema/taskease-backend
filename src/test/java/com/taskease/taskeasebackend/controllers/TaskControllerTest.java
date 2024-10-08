package com.taskease.taskeasebackend.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.taskease.taskeasebackend.dto.request.CreateTaskForProjectRequest;
import com.taskease.taskeasebackend.dto.response.TaskDTO;
import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

 class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCreateTask() {
        Task task = new Task();
        Task createdTask = new Task();

        when(taskService.createTask(task)).thenReturn(createdTask);

        ResponseEntity<Task> response = taskController.createTask(task);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdTask, response.getBody());
    }

    @Test
     void testCreateTaskForProject() {
        Long projectId = 1L;
        CreateTaskForProjectRequest request = new CreateTaskForProjectRequest();
        TaskDTO taskDTO = new TaskDTO();

        when(taskService.createTaskForProject(projectId, request)).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = taskController.createTaskForProject(projectId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taskDTO, response.getBody());
    }

    @Test
     void testDeleteTaskById() {
        Long taskId = 1L;

        doNothing().when(taskService).deleteTaskById(taskId);

        ResponseEntity<Void> response = taskController.deleteTaskById(taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
     void testUpdateTask() {
        Long taskId = 1L;
        Task task = new Task();
        TaskDTO taskDTO = new TaskDTO();

        when(taskService.updateTask(taskId, task)).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = taskController.updateTask(taskId, task);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDTO, response.getBody());
    }

    @Test
     void testGetTasksByUserId() {
        Long userId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        List<TaskDTO> taskDTOs = Collections.singletonList(taskDTO);

        when(taskService.getTasksByUserId(userId)).thenReturn(taskDTOs);

        ResponseEntity<List<TaskDTO>> response = taskController.getTasksByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDTOs, response.getBody());
    }

    @Test
     void testGetTasksFromProject() {
        Long projectId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        List<TaskDTO> taskDTOs = Collections.singletonList(taskDTO);

        when(taskService.getTasksFromProject(projectId)).thenReturn(taskDTOs);

        ResponseEntity<List<TaskDTO>> response = taskController.getTasksFromProject(projectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDTOs, response.getBody());
    }
}
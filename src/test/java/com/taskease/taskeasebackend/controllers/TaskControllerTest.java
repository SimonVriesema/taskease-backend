package com.taskease.taskeasebackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskease.taskeasebackend.dto.request.CreateTaskForProjectRequest;
import com.taskease.taskeasebackend.dto.response.TaskDTO;
import com.taskease.taskeasebackend.exceptions.ProjectNotFoundException;
import com.taskease.taskeasebackend.exceptions.TaskNotFoundException;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.TaskService;
import com.taskease.taskeasebackend.services.UserService;
import com.taskease.taskeasebackend.utils.DTOConvertor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTask() throws Exception {
        Task task = new Task();
        Task createdTask = new Task();
        Mockito.when(taskService.createTask(task)).thenReturn(createdTask);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdTask.getId()));
    }

    @Test
    void testCreateTaskForProject_ProjectNotFound() throws Exception {
        Long projectId = 1L;
        CreateTaskForProjectRequest request = new CreateTaskForProjectRequest();
        Mockito.doThrow(new ProjectNotFoundException("Project not found")).when(taskService).createTaskForProject(projectId, request);

        mockMvc.perform(post("/tasks/project/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTaskForProject_UserNotFound() throws Exception {
        Long projectId = 1L;
        CreateTaskForProjectRequest request = new CreateTaskForProjectRequest();
        Mockito.doThrow(new UserNotFoundException("User not found")).when(taskService).createTaskForProject(projectId, request);

        mockMvc.perform(post("/tasks/project/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTaskForProject_Success() throws Exception {
        Long projectId = 1L;
        CreateTaskForProjectRequest request = new CreateTaskForProjectRequest();
        Task task = new Task();
        TaskDTO taskDTO = new TaskDTO();
        Mockito.when(taskService.createTaskForProject(projectId, request)).thenReturn(task);

        mockMvc.perform(post("/tasks/project/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskDTO.getId()));
    }

    @Test
    void testDeleteTaskById_Success() throws Exception {
        Long taskId = 1L;
        Mockito.doNothing().when(taskService).deleteTaskById(taskId);

        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteTaskById_NotFound() throws Exception {
        Long taskId = 1L;
        Mockito.doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTaskById(taskId);

        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTask_Success() throws Exception {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(taskId);

        Mockito.when(taskService.updateTask(task)).thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskDTO.getId()));
    }

    @Test
    void testGetTasksByUserId_Success() throws Exception {
        Long userId = 1L;
        User user = new User();
        List<Task> tasks = Collections.singletonList(new Task());
        List<TaskDTO> taskDTOs = Collections.singletonList(new TaskDTO());
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(taskService.getTasksByAssignedUser(user)).thenReturn(tasks);

        mockMvc.perform(get("/tasks/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(taskDTOs.get(0).getId()));
    }

    @Test
    void testGetTasksByUserId_NotFound() throws Exception {
        Long userId = 1L;
        Mockito.when(userService.getUserById(userId)).thenReturn(null);

        mockMvc.perform(get("/tasks/user/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTasksFromProject_Success() throws Exception {
        Long projectId = 1L;
        List<TaskDTO> taskDTOs = Collections.singletonList(new TaskDTO());
        Mockito.when(taskService.getTasksFromProject(projectId)).thenReturn(taskDTOs);

        mockMvc.perform(get("/tasks/project/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(taskDTOs.get(0).getId()));
    }

    @Test
    void testGetTasksFromProject_NotFound() throws Exception {
        Long projectId = 1L;
        Mockito.when(taskService.getTasksFromProject(projectId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks/project/{id}", projectId))
                .andExpect(status().isNotFound());
    }
}
package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.dto.request.CreateTaskForProjectRequest;
import com.taskease.taskeasebackend.dto.response.TaskDTO;
import com.taskease.taskeasebackend.exceptions.ProjectNotFoundException;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.TaskService;
import com.taskease.taskeasebackend.services.UserService;
import com.taskease.taskeasebackend.utils.DTOConvertor;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation(value = "Create a task and store in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the task"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        try {
            Task createdTask = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/project/{projectId}")
    @ApiOperation(value = "Create a task for an existing project and store in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the task"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 404, message = "User or project not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<TaskDTO> createTaskForProject(@PathVariable Long projectId, @RequestBody CreateTaskForProjectRequest createTaskRequest) {
        try {
            Task createdTask = taskService.createTaskForProject(projectId, createTaskRequest);
            TaskDTO taskDTO = DTOConvertor.convertToDTO(createdTask);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete task by ID", notes = "Delete task by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the task"),
            @ApiResponse(code = 404, message = "The task you were trying to delete is not found"),
    })
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        try {
            taskService.deleteTaskById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update task by ID", notes = "Update task by providing its ID and new data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the task"),
            @ApiResponse(code = 404, message = "The task you were trying to update is not found"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            task.setId(id);
            Task updatedTask = taskService.updateTask(task);
            TaskDTO taskDTO = DTOConvertor.convertToDTO(updatedTask);
            return ResponseEntity.status(HttpStatus.OK).body(taskDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get tasks by user ID", notes = "Retrieve tasks assigned to a user by providing their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the tasks"),
            @ApiResponse(code = 404, message = "The user you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            List<Task> tasks = taskService.getTasksByAssignedUser(user);
            List<TaskDTO> taskDTOs = tasks.stream()
                    .map(DTOConvertor::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(taskDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/project/{id}")
    @ApiOperation(value = "Get tasks from a project by project ID", notes = "Retrieve tasks associated with a project by providing the project ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the tasks"),
            @ApiResponse(code = 404, message = "Project not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<TaskDTO>> getTasksFromProject(@PathVariable Long id) {
        try {
            List<TaskDTO> taskDTOs = taskService.getTasksFromProject(id);
            if (taskDTOs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(taskDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
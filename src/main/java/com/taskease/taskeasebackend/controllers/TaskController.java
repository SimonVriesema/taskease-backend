package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.TaskService;
import com.taskease.taskeasebackend.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete task by ID", notes = "Delete task by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the task"),
            @ApiResponse(code = 404, message = "The task you were trying to delete is not found"),
    })
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        if (taskService.getTaskById(id) != null) {
            taskService.deleteTaskById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
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
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            Task existingTask = taskService.getTaskById(id);
            if (existingTask != null) {
                task.setId(id);
                Task updatedTask = taskService.updateTask(task);
                return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
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
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                List<Task> tasks = taskService.getTasksByAssignedUser(user);
                return ResponseEntity.ok(tasks);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.services.TaskService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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
}

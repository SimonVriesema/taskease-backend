package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation(value = "Create a user and store in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the user"),
    })
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find user by ID", notes = "Retrieve user details by providing their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the user"),
            @ApiResponse(code = 404, message = "The user you were trying to reach is not found"),
    })
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

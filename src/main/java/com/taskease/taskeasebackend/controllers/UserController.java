package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.models.User;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

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

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by ID", notes = "Delete user by providing their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the user"),
            @ApiResponse(code = 404, message = "The user you were trying to delete is not found"),
    })
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/email/{email}")
    @ApiOperation(value = "Find user by email", notes = "Retrieve user details by providing their email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the user"),
            @ApiResponse(code = 404, message = "The user you were trying to reach is not found"),
    })
    public ResponseEntity<User> findUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    @ApiOperation(value = "Find user by username", notes = "Retrieve user details by providing their username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the user"),
            @ApiResponse(code = 404, message = "The user you were trying to reach is not found"),
    })
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exists/email/{email}")
    @ApiOperation(value = "Check if user exists by email", notes = "Check if a user exists by providing their email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully checked the user existence"),
            @ApiResponse(code = 404, message = "The user you were trying to check is not found"),
    })
    public ResponseEntity<Boolean> doesUserExistByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user != null);
    }
}

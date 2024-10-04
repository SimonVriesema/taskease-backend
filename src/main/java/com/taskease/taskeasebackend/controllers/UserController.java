package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        try {
            UserDTO userDTO = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @ApiOperation(value = "Get all users", notes = "Retrieve a list of all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the users"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> userDTOs = userService.getAllUsers();
            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find user by ID", notes = "Retrieve user details by providing their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the user"),
            @ApiResponse(code = 404, message = "The user you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            return ResponseEntity.ok(userDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by ID", notes = "Delete user by providing their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the user"),
            @ApiResponse(code = 404, message = "The user you were trying to delete is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/email/{email}")
    @ApiOperation(value = "Find user by email", notes = "Retrieve user details by providing their email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the user"),
            @ApiResponse(code = 404, message = "The user you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
        try {
            UserDTO userDTO = userService.getUserByEmail(email);
            return ResponseEntity.ok(userDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/username/{username}")
    @ApiOperation(value = "Find user by username", notes = "Retrieve user details by providing their username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the user"),
            @ApiResponse(code = 404, message = "The user you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable String username) {
        try {
            UserDTO userDTO = userService.getUserByUsername(username);
            return ResponseEntity.ok(userDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update user by ID", notes = "Update user details by providing their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the user"),
            @ApiResponse(code = 404, message = "The user you were trying to update is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody User user) {
        try {
            UserDTO userDTO = userService.updateUser(id, user);
            return ResponseEntity.ok(userDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
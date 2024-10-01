package com.taskease.taskeasebackend.controllers;

import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.UserService;
import com.taskease.taskeasebackend.utils.DTOConvertor;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        UserDTO userDTO = DTOConvertor.convertToDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping
    @ApiOperation(value = "Get all users", notes = "Retrieve a list of all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the users"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOs = users.stream()
                    .map(DTOConvertor::convertToDTO)
                    .collect(Collectors.toList());
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
    })
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            UserDTO userDTO = DTOConvertor.convertToDTO(user);
            return ResponseEntity.ok(userDTO);
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
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            UserDTO userDTO = DTOConvertor.convertToDTO(user);
            return ResponseEntity.ok(userDTO);
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
    public ResponseEntity<UserDTO> findUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            UserDTO userDTO = DTOConvertor.convertToDTO(user);
            return ResponseEntity.ok(userDTO);
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

    @PutMapping("/{id}")
    @ApiOperation(value = "Update user by ID", notes = "Update user details by providing their ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the user"),
            @ApiResponse(code = 404, message = "The user you were trying to update is not found"),
    })
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            UserDTO userDTO = DTOConvertor.convertToDTO(updatedUser);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
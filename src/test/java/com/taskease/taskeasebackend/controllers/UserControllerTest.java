package com.taskease.taskeasebackend.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOs = Collections.singletonList(userDTO);

        when(userService.getAllUsers()).thenReturn(userDTOs);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOs, response.getBody());
    }

    @Test
    void testFindUserById() throws UserNotFoundException {
        UserDTO userDTO = new UserDTO();

        when(userService.getUserById(1L)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.findUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testFindUserById_NotFound() throws UserNotFoundException {
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserDTO> response = userController.findUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUserById() throws UserNotFoundException {
        doNothing().when(userService).deleteUserById(1L);

        ResponseEntity<Void> response = userController.deleteUserById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteUserById_NotFound() throws UserNotFoundException {
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUserById(1L);

        ResponseEntity<Void> response = userController.deleteUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindUserByEmail() throws UserNotFoundException {
        UserDTO userDTO = new UserDTO();

        when(userService.getUserByEmail("test@example.com")).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.findUserByEmail("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testFindUserByEmail_NotFound() throws UserNotFoundException {
        when(userService.getUserByEmail("test@example.com")).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserDTO> response = userController.findUserByEmail("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindUserByUsername() throws UserNotFoundException {
        UserDTO userDTO = new UserDTO();

        when(userService.getUserByUsername("testuser")).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.findUserByUsername("testuser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testFindUserByUsername_NotFound() throws UserNotFoundException {
        when(userService.getUserByUsername("testuser")).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserDTO> response = userController.findUserByUsername("testuser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUserById() throws UserNotFoundException {
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userService.updateUser(1L, user)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.updateUserById(1L, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testUpdateUserById_NotFound() throws UserNotFoundException {
        User user = new User();

        when(userService.updateUser(1L, user)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<UserDTO> response = userController.updateUserById(1L, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
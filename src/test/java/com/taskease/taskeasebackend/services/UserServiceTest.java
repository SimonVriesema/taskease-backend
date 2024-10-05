package com.taskease.taskeasebackend.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById_UserExists() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserById(1L);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.save(user)).thenReturn(user);

        UserDTO userDTO = userService.createUser(user);

        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUsername());
    }

    @Test
    public void testDeleteUserById() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetUserByUsername_UserExists() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserByUsername("testuser");

        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUsername());
    }

    @Test
    public void testGetUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("testuser"));
    }

    @Test
    public void testGetUserByEmail_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserByEmail("test@example.com");

        assertNotNull(userDTO);
        assertEquals("test@example.com", userDTO.getEmail());
    }

    @Test
    public void testGetUserByEmail_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("test@example.com"));
    }

    @Test
    public void testUpdateUser_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("updateduser");
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO userDTO = userService.updateUser(1L, user);

        assertNotNull(userDTO);
        assertEquals("updateduser", userDTO.getUsername());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        User user = new User();
        user.setId(1L);
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, user));
    }
}
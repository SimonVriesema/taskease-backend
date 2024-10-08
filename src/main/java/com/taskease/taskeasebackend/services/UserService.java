package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.dto.request.LoginRequest;
import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.UserRepository;
import com.taskease.taskeasebackend.utils.DTOConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public UserDTO getUserById(Long id) {
        User user =  userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return DTOConvertor.convertToDTO(user);
    }

    public User getUserEntityById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDTO createUser(User user) {
        User createdUser = userRepository.save(user);
        return DTOConvertor.convertToDTO(createdUser);
    }

    public void deleteUserById(Long id) {userRepository.deleteById(id);}

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return DTOConvertor.convertToDTO(user);
    }

    public UserDTO login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("User not found");
        }
        return DTOConvertor.convertToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return DTOConvertor.convertToDTO(user);
    }

    public UserDTO updateUser(Long id, User user) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        user.setId(id);
        User updatedUser = userRepository.save(user);
        return DTOConvertor.convertToDTO(updatedUser);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(DTOConvertor::convertToDTO)
                .collect(Collectors.toList());
    }}

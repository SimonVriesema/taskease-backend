package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.dto.response.UserDTO;
import com.taskease.taskeasebackend.exceptions.UserNotFoundException;
import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.UserRepository;
import com.taskease.taskeasebackend.utils.DTOConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

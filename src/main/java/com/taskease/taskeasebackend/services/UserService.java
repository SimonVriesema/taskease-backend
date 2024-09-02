package com.taskease.taskeasebackend.services;

import com.taskease.taskeasebackend.models.User;
import com.taskease.taskeasebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUserById(Long id) {userRepository.deleteById(id);}
    public User getUserByUsername(String username) { return userRepository.findByUsername(username).orElse(null);}
    public User getUserByEmail(String email) { return userRepository.findByEmail(email).orElse(null);}

    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        } else {
            return null;
        }
    }
}

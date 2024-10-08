package com.taskease.taskeasebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {
    private UserDTO user;
    private String token;
}
package com.taskease.taskeasebackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/kaas")
    public String index() {
        return "Je vader is een hamster en je moeder ruikt naar vlierbessen!";
    }
}

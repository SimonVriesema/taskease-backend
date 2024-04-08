package com.taskease.taskeasebackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String firstName;
    String preposition;
    @Column(nullable = false)
    String lastName;
    String description;
    String dateOfBirth;
    String jobTitle;
}

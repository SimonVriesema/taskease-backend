package com.taskease.taskeasebackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    String firstName;
    String preposition;
    String lastName;
    String description;
    String dateOfBirth;
    String jobTitle;
}

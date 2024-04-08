package com.taskease.taskeasebackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String preposition;
    private String lastName;
    private String description;
    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "job_title")
    private String jobTitle;

    public User() {
    }

    public User(String email, String firstName, String preposition, String lastName, String description, String dateOfBirth, String jobTitle) {
        this.email = email;
        this.firstName = firstName;
        this.preposition = preposition;
        this.lastName = lastName;
        this.description = description;
        this.dateOfBirth = dateOfBirth;
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", preposition='" + preposition + '\'' +
                ", lastName='" + lastName + '\'' +
                ", description='" + description + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}


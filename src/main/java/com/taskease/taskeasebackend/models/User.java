package com.taskease.taskeasebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Email
    @Column(unique = true)
    private String email;
    @Size(max = 50)
    @Column(unique = true)
    private String username;
    @Size(max = 50)
    private String firstName;
    @Size(max = 50)
    private String preposition;
    @Size(max = 50)
    private String lastName;
    @Size(max = 255)
    private String description;
    private LocalDate dateOfBirth;
    @Size(max = 100)
    private String jobTitle;
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Project> projects = new ArrayList<>();
    @OneToMany(mappedBy = "assignedUser")
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();
}


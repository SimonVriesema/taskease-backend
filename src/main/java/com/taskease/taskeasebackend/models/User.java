package com.taskease.taskeasebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.taskease.taskeasebackend.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User{
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
    @NotNull
    private String password;
}


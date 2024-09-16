package com.taskease.taskeasebackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taskease.taskeasebackend.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Project title cannot be null or empty")
    @Size(max = 50)
    @JsonProperty("title")
    @Column(nullable = false)
    private String title;

    @Size(max = 500)
    @JsonProperty("description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_leader_id")
    @JsonProperty("projectLeader")
    private User projectLeader;

    @Column(nullable = false, updatable = false)
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("status")
    private Status status;

    @ManyToMany(mappedBy = "projects")
    private List<User> users = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDate.from(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        if (createdDate == null) {
            createdDate = LocalDate.from(LocalDateTime.now());
        }
    }
}
package com.taskease.taskeasebackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taskease.taskeasebackend.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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

    @Size(max = 50)
    @JsonProperty("title")
    @Column(nullable = false)
    private String title;

    @Size(max = 500)
    @JsonProperty("description")
    private String description;

//    @OneToMany
//    @JoinTable(
//            name = "project_tasks",
//            joinColumns = @JoinColumn(name = "project_id"),
//            inverseJoinColumns = @JoinColumn(name = "task_id")
//    )
//    private List<Task> tasks;
//
//    @ManyToMany
//    @JoinTable(
//            name = "project_users",
//            joinColumns = @JoinColumn(name = "project_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    private List<User> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "project_leader_id")
    private User projectLeader;

    @Column(nullable = false, updatable = false)
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

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

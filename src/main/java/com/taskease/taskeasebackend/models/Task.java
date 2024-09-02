package com.taskease.taskeasebackend.models;

import com.taskease.taskeasebackend.enums.Priority;
import com.taskease.taskeasebackend.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(nullable = false)
    private String title;

    @Size(max = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    private LocalDate dueDate;

    @Column(nullable = false, updatable = false)
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

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

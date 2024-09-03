package com.taskease.taskeasebackend.models.messages;

import com.taskease.taskeasebackend.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDate createdDate;

    @Size(max = 500)
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User sender;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDate.from(LocalDateTime.now());
    }
}

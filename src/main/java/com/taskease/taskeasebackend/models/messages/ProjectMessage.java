package com.taskease.taskeasebackend.models.messages;

import com.taskease.taskeasebackend.models.Project;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "project_messages")
public class ProjectMessage extends Message {
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}

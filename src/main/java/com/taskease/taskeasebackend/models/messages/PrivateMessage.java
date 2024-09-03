package com.taskease.taskeasebackend.models.messages;

import com.taskease.taskeasebackend.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "private_messages")
public class PrivateMessage extends Message {
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
}

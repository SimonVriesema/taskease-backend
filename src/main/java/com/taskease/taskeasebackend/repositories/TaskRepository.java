package com.taskease.taskeasebackend.repositories;

import com.taskease.taskeasebackend.enums.Priority;
import com.taskease.taskeasebackend.enums.Status;
import com.taskease.taskeasebackend.models.Task;
import com.taskease.taskeasebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Override
    <S extends Task> S save(S task);
    @Override
    void deleteById(Long id);
    List<Task> findByStatus(Status status);
    List<Task> findByPriority(Priority priority);

    List<Task> findByDueDate(LocalDate dueDate);

    List<Task> findByAssignedUser(User assignedUser);

    List<Task> findByTitleContaining(String keyword);
}

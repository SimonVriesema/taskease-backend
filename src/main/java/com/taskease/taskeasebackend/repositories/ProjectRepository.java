package com.taskease.taskeasebackend.repositories;

import com.taskease.taskeasebackend.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Override
    <S extends Project> S save(S entity);

    @Override
    Optional<Project> findById(Long aLong);

    @Override
    void deleteById(Long aLong);

    @Override
    boolean existsById(Long aLong);

    List<Project> findByProjectLeader_Id(Long projectLeaderId);
}

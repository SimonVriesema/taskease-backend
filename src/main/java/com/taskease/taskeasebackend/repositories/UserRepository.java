package com.taskease.taskeasebackend.repositories;

import com.taskease.taskeasebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    <S extends User> S save(S user);

    @Override
    Optional<User> findById(Long id);

    //TODO: Create Endpoint
    @Override
    List<User> findAll();

    //TODO: Create Endpoint
    @Override
    void delete(User user);

    //TODO: Create Endpoint
    @Override
    void deleteById(Long id);

    //TODO: Create Endpoint
    Optional<User> findByUsername(String username);

    //TODO: Create Endpoint
    Optional<User> findByEmail(String email);

    //TODO: Create Endpoint
    boolean existsByEmail(String email);
}

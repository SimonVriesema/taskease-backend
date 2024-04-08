package com.taskease.taskeasebackend.repositories;

import com.taskease.taskeasebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    <S extends User> S save(S user);
    @Override
    Optional<User> findById(Long aLong);
    User createUser(User user);
}

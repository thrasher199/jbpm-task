package com.jbpmtask.application.data.service;

import com.jbpmtask.application.data.entity.User;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    boolean existsByUsername(String userId);

    User findOneByUsername(String userId);

    List<User> findByRoles_Name(String name);
}
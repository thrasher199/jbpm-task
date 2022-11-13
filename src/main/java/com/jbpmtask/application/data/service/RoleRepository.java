package com.jbpmtask.application.data.service;

import com.jbpmtask.application.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsRoleByName(String groupId);
}

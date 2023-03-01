package com.jbpmtask.application.data.service;

import com.jbpmtask.application.data.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
}
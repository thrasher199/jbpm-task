package com.jbpmtask.application.data.service;

import com.jbpmtask.application.data.entity.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, UUID> {
}
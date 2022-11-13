package com.jbpmtask.application.data.dto;

import com.jbpmtask.application.data.entity.Role;

import java.util.Set;

public class UserDto {

    private String username;
    private String name;
    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

package com.jbpmtask.application.data.endpoint;

import com.jbpmtask.application.data.entity.Role;
import com.jbpmtask.application.data.service.RoleRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;

import java.util.List;
import java.util.UUID;

@Endpoint
@AnonymousAllowed
public class RoleEndpoint {

    private final RoleRepository roleRepository;

    public RoleEndpoint(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Nonnull
    public List<@Nonnull Role> list(){
        return roleRepository.findAll();
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public void delete(UUID id){
        roleRepository.deleteById(id);
    }
}

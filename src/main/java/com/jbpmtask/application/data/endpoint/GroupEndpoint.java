package com.jbpmtask.application.data.endpoint;

import com.jbpmtask.application.data.entity.Group;
import com.jbpmtask.application.data.service.GroupRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;

import java.util.List;
import java.util.UUID;

@Endpoint
@AnonymousAllowed
public class GroupEndpoint {

    private final GroupRepository groupRepository;

    public GroupEndpoint(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Nonnull
    public List<@Nonnull Group> list(){
        return groupRepository.findAll();
    }

    public Group save(Group group){
        return groupRepository.save(group);
    }

    public void delete(UUID id){
        groupRepository.deleteById(id);
    }
}

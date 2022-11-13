package com.jbpmtask.application.configuration.jbpm;

import com.jbpmtask.application.data.entity.User;
import com.jbpmtask.application.data.service.UserRepository;
import org.kie.api.task.model.Group;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.internal.task.api.TaskModelProvider;
import org.kie.internal.task.api.UserInfo;
import org.kie.internal.task.api.model.InternalOrganizationalEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomUserInfo implements UserInfo {

    private final UserRepository userRepository;

    public CustomUserInfo(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getDisplayName(OrganizationalEntity entity) {
        return null;
    }

    @Override
    public Iterator<OrganizationalEntity> getMembersForGroup(Group group) {
        List<User> userList = userRepository.findByRoles_Name(group.getId());
        List<OrganizationalEntity> organizationalEntityList = new ArrayList<>();
        userList.forEach(x -> {
            org.kie.api.task.model.User user = TaskModelProvider.getFactory().newUser();
            ((InternalOrganizationalEntity) user).setId(x.getUsername());
            organizationalEntityList.add(user);
        });

        return organizationalEntityList.iterator();
    }

    @Override
    public boolean hasEmail(Group group) {
        return false;
    }

    @Override
    public String getEmailForEntity(OrganizationalEntity entity) {
        return null;
    }

    @Override
    public String getLanguageForEntity(OrganizationalEntity entity) {
        return null;
    }

    @Override
    public String getEntityForEmail(String s) {
        return null;
    }
}

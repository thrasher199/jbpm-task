package com.jbpmtask.application.configuration.jbpm;


import com.jbpmtask.application.data.entity.Role;
import com.jbpmtask.application.data.entity.User;
import com.jbpmtask.application.data.service.RoleRepository;
import com.jbpmtask.application.data.service.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.task.UserGroupCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserGroupCallback implements UserGroupCallback {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomUserGroupCallback(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean existsUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return userRepository.existsByUsername(userId);
    }

    @Override
    public boolean existsGroup(String groupId) {
        //groupId in JBPM is a role.name in application
        if(StringUtils.isEmpty(groupId)){
            throw new IllegalArgumentException("GroupId cannot be null");
        }
        return roleRepository.existsRoleByName(groupId);
    }

    @Override
    public List<String> getGroupsForUser(String userId) {
        List<String> roleList = new ArrayList<>();
        if (StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        User user = userRepository.findOneByUsername(userId);
        if(ObjectUtils.anyNotNull(user)){
            Set<Role> userRoles = user.getRoles();
            roleList = userRoles.stream().map(Role::getName).collect(Collectors.toList());
        }

        return roleList;
    }
}

package com.jbpmtask.application.data.endpoint;

import com.jbpmtask.application.data.entity.Role;
import com.jbpmtask.application.data.entity.User;
import com.jbpmtask.application.data.service.RoleRepository;
import com.jbpmtask.application.data.service.UserService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Endpoint
@AnonymousAllowed
public class UserCrudEndpoint {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserCrudEndpoint(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Nonnull
    public Page<@Nonnull User> list(Pageable pageable){
        return userService.list(pageable);
    }

    public Optional<User> get(@Nonnull UUID id){
        return userService.get(id);
    }

    @Nonnull
    public User update(@Nonnull User user){
        return userService.update(user);
    }

    public void delete(@Nonnull UUID id){
        userService.delete(id);
    }

    public int count(){
        return userService.count();
    }

    @Nonnull
    public List<@Nonnull Role> roleList(){
        return roleRepository.findAll();
    }

    @Nonnull
    public List<@Nonnull String> roleListStr(){
        return  roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
    }
}

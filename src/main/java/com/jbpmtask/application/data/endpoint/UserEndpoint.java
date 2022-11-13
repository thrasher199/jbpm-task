package com.jbpmtask.application.data.endpoint;

import com.jbpmtask.application.data.dto.UserDto;
import com.jbpmtask.application.data.entity.User;
import com.jbpmtask.application.security.AuthenticatedUser;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
@AnonymousAllowed
public class UserEndpoint {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private ModelMapper modelMapper;

    public Optional<UserDto> getAuthenticatedUser() {
        return converToDto(authenticatedUser.get());
    }

    private Optional<UserDto> converToDto(Optional<User> user){
        UserDto userDto = null;
        if(user.isPresent()){
            userDto = modelMapper.map(user.get(), UserDto.class);
        }
        return Optional.ofNullable(userDto);
    }
}

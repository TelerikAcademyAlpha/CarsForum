package com.backend.forum.mapper;

import com.backend.forum.models.User;
import com.backend.forum.models.dtos.request.RegisterRequest;
import com.backend.forum.models.dtos.request.UpdateUserRequest;
import com.backend.forum.models.dtos.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest dto) {

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        return user;
    }

    public UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setBlocked(user.isBlocked());

        return response;
    }

    public void updateEntity(UpdateUserRequest request, User user) {

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
    }
}

package com.backend.carsforum.services;

import com.backend.carsforum.models.User;
import com.backend.carsforum.models.dtos.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    User register(User user);

    User getById(Long id);

    User getByUsername(String username);

    User getByEmail(String email);

    List<User> getAll();

    User update(User user);

    void delete(Long id);

    void block(Long id);

    void unblock(Long id);

    void promoteToAdmin(Long id);

    long getUsersCount();

    User updateProfile(UpdateUserRequest request);
}
package com.backend.carsforum.services;

import com.backend.carsforum.models.User;

public interface UserService {
    User getById(Long id);
    User getByUsername(String username);
    void register(User user);
    void update(User user);
    void delete(Long id);
}
package com.backend.forum.repositories;

import com.backend.forum.models.User;

import java.util.List;

public interface UserRepository {
    User findById(Long id);
    void save(User user);
    void update(User user);
    void delete(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long countAll();
    List<User> findAll();
}

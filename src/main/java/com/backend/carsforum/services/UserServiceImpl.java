package com.backend.carsforum.services;

import com.backend.carsforum.models.User;
import com.backend.carsforum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " not found.");
        }
        return user;
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username '" + username + "' not found.");
        }
        return user;
    }

    @Override
    public void register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already taken.");
        }
        userRepository.save(user);
    }

    @Override
    public void update(User userToUpdate) {
        getById(userToUpdate.getId());

        User userWithEmail = userRepository.findByEmail(userToUpdate.getEmail());
        if (userWithEmail != null && !userWithEmail.getId().equals(userToUpdate.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already taken.");
        }

        userRepository.update(userToUpdate);
    }

    @Override
    public void delete(Long id) {
        User userToDelete = getById(id);
        userRepository.delete(userToDelete);
    }
}
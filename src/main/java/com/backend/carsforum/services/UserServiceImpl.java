package com.backend.carsforum.services;

import com.backend.carsforum.exceptions.EntityDuplicateException;
import com.backend.carsforum.models.Role;
import com.backend.carsforum.models.User;
import com.backend.carsforum.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())){
            throw new EntityDuplicateException("User","username",user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())){
            throw new EntityDuplicateException("User","email",user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getById(Long id) {
        return  userRepository.findById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void block(Long id) {
        User user = getExistingUser(id);
        user.setBlocked(true);
        userRepository.update(user);
    }

    @Override
    @Transactional
    public void unblock(Long id) {
        User user = getExistingUser(id);
        user.setBlocked(false);
        userRepository.update(user);
    }

    @Override
    @Transactional
    public void promoteToAdmin(Long id) {
        User user = getExistingUser(id);
        user.setRole(Role.ADMIN);
        userRepository.update(user);
    }

    @Override
    public long getUsersCount() {
        return userRepository.countAll();
    }

    private User getExistingUser(Long id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        return user;
    }

    @Override
    @Transactional
    public User update(User updatedUser) {
        User existing = getExistingUser(updatedUser.getId());

        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setEmail(updatedUser.getEmail());

        userRepository.update(existing);

        return existing;
    }


}
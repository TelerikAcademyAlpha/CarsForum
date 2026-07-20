package com.backend.forum.services;

import com.backend.forum.exceptions.EntityDuplicateException;
import com.backend.forum.exceptions.EntityNotFoundException;
import com.backend.forum.mapper.UserMapper;
import com.backend.forum.models.Role;
import com.backend.forum.models.User;
import com.backend.forum.models.dtos.request.UpdateUserRequest;
import com.backend.forum.repositories.UserRepository;
import com.backend.forum.security.AuthenticationFacade;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
        this.userMapper = userMapper;
    }

    @Override
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getById(Long id) {
        return getExistingUser(id);
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("User", "email", email);
        }

        return user;
    }

    @Override
    public User updateProfile(UpdateUserRequest request) {

        User current = getCurrentUser();

        // duplicate email check
        if (!current.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new EntityDuplicateException(
                    "User",
                    "email",
                    request.getEmail());
        }

        userMapper.updateEntity(request, current);

        userRepository.update(current);

        return current;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = getExistingUser(id);

        userRepository.delete(user);
    }

    @Override
    public void block(Long id) {
        User user = getExistingUser(id);
        user.setBlocked(true);
        userRepository.update(user);
    }

    @Override
    public void unblock(Long id) {
        User user = getExistingUser(id);
        user.setBlocked(false);
        userRepository.update(user);
    }

    @Override
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
            throw new EntityNotFoundException("User", id);
        }

        return user;
    }

    @Override
    public User update(User updatedUser) {
        User existing = getExistingUser(updatedUser.getId());

        // Only check if the email is actually changing
        if (!existing.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) {

            throw new EntityDuplicateException(
                    "User",
                    "email",
                    updatedUser.getEmail());
        }

        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setEmail(updatedUser.getEmail());

        userRepository.update(existing);

        return existing;
    }

    public User getCurrentUser() {
        String username = authenticationFacade.getCurrentUsername();
        return getByUsername(username);
    }

}
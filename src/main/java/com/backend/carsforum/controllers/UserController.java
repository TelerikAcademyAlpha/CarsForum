package com.backend.carsforum.controllers;

import com.backend.carsforum.models.User;
import com.backend.carsforum.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/search")
    public User getByUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody User user) {
        userService.register(user);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody User userToUpdate) {
        if (!String.valueOf(id).equals(String.valueOf(userToUpdate.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path and body do not match.");
        }
        userService.update(userToUpdate);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
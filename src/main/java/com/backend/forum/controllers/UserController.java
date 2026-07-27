package com.backend.forum.controllers;

import com.backend.forum.exceptions.EntityDuplicateException;
import com.backend.forum.mapper.UserMapper;
import com.backend.forum.models.User;
import com.backend.forum.models.dtos.request.RegisterRequest;
import com.backend.forum.models.dtos.request.UpdateUserRequest;
import com.backend.forum.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterRequest request,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("showRegister", true);

            return "index";
        }

        try {

            User user = userMapper.toEntity(request);

            userService.register(user);

        } catch (EntityDuplicateException ex) {

            model.addAttribute("showRegister", true);
            model.addAttribute("registrationError", ex.getMessage());

            return "index";
        }

        return "redirect:/";
    }

    @GetMapping("/users/profile")
    public String profile(
            Authentication authentication,
            Model model) {

        User user = userService.getByUsername(authentication.getName());

        model.addAttribute("user", user);

        return "users/profile";
    }

    @PostMapping("/users/profile")
    public String updateProfile(
            @ModelAttribute UpdateUserRequest request) {

        userService.updateProfile(request);

        return "redirect:/users/profile";
    }
}
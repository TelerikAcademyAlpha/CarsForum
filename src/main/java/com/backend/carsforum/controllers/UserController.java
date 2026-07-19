package com.backend.carsforum.controllers;

import com.backend.carsforum.exceptions.EntityDuplicateException;
import com.backend.carsforum.mapper.UserMapper;
import com.backend.carsforum.models.User;
import com.backend.carsforum.models.dtos.request.RegisterRequest;
import com.backend.carsforum.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
}
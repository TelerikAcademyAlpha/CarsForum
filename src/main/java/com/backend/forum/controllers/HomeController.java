package com.backend.forum.controllers;

import com.backend.forum.models.dtos.request.CreatePostRequest;
import com.backend.forum.models.dtos.request.RegisterRequest;
import com.backend.forum.services.PostService;
import com.backend.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public HomeController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHomePage(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("loginError", error != null);

        model.addAttribute("latestPosts", postService.getLatest());

        model.addAttribute("topCommentedPosts", postService.getTopCommented());

        model.addAttribute("userCount", userService.getAll().size());

        model.addAttribute("postCount", postService.findAll().size());

        model.addAttribute("registerRequest", new RegisterRequest());

        model.addAttribute(
                "createPostRequest",
                new CreatePostRequest());

        return "index";
    }
}
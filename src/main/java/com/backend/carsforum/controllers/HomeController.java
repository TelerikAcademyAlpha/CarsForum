package com.backend.carsforum.controllers;

import com.backend.carsforum.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PostService postService;

    @Autowired
    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("posts", postService.getLatest());
        model.addAttribute("userCount", 15);
        model.addAttribute("postCount", 42);

        return "index";
    }
}
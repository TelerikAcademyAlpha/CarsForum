package com.backend.forum.controllers;

import com.backend.forum.services.CommentService;
import com.backend.forum.services.PostService;
import com.backend.forum.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public AdminController(UserService userService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute(
                "users",
                userService.getAll());


        model.addAttribute("usersCount", userService.getAll().size());

        model.addAttribute("postsCount", postService.findAll().size());

        model.addAttribute("commentsCount", commentService.getAll().size());

        return "admin/dashboard";
    }

    @PostMapping("/promote/{id}")
    public String promote(@PathVariable Long id) {

        userService.promoteToAdmin(id);

        return "redirect:/admin";
    }


    @PostMapping("/demote/{id}")
    public String demote(@PathVariable Long id) {

        userService.demoteToUser(id);

        return "redirect:/admin";
    }

    @PostMapping("/block/{id}")
    public String block(@PathVariable Long id) {

        userService.block(id);

        return "redirect:/admin";
    }

    @PostMapping("/unblock/{id}")
    public String unblock(@PathVariable Long id) {

        userService.unblock(id);

        return "redirect:/admin";
    }

}

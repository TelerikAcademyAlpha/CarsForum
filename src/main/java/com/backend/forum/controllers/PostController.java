package com.backend.forum.controllers;

import com.backend.forum.mapper.PostMapper;
import com.backend.forum.models.Post;
import com.backend.forum.models.dtos.request.CreateCommentRequest;
import com.backend.forum.models.dtos.request.CreatePostRequest;
import com.backend.forum.models.dtos.request.RegisterRequest;
import com.backend.forum.models.dtos.response.PostResponse;
import com.backend.forum.services.LikeService;
import com.backend.forum.services.PostService;
import com.backend.forum.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;
    private final LikeService likeService;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper, UserService userService, LikeService likeService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.userService = userService;
        this.likeService = likeService;
    }

    @PostMapping("/create")
    public String create(
            @Valid
            @ModelAttribute CreatePostRequest request,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("showCreatePost", true);

            // keep entered values
            model.addAttribute("createPostRequest", request);

            // everything index.html expects
            model.addAttribute("registerRequest", new RegisterRequest());

            model.addAttribute("latestPosts", postService.getLatest());

            model.addAttribute("topCommentedPosts", postService.getTopCommented());

            model.addAttribute("postCount", postService.findAll().size());

            model.addAttribute("userCount", userService.getAll().size());

            return "index";
        }

        Post post = postMapper.toEntity(request);

        postService.create(post);

        return "redirect:/";
    }

    @GetMapping("/latest")
    public String latestPosts(Model model) {

        model.addAttribute("posts", postService.getLatest());

        return "posts/latest";
    }

    @GetMapping("/{id}")
    public String details(
            @PathVariable Long id,
            Model model) {

        Post post = postService.getById(id);

        model.addAttribute("post", post);

        model.addAttribute(
                "liked",
                likeService.isLiked(id));

        model.addAttribute(
                "createCommentRequest",
                new CreateCommentRequest());

        return "posts/details";
    }

    @GetMapping("/top-commented")
    public String topCommented(Model model) {

        model.addAttribute(
                "posts",
                postService.getTopCommented());

        return "posts/top-commented";
    }

    @GetMapping
    public String allPosts(Model model) {

        model.addAttribute(
                "posts",
                postService.findAll());

        return "posts/allPosts";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam String keyword,
            Model model) {

        model.addAttribute(
                "posts",
                postService.search(keyword));

        model.addAttribute(
                "keyword",
                keyword);

        return "posts/search";
    }
}
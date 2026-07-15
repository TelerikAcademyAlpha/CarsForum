package com.backend.carsforum.controllers;

import com.backend.carsforum.models.Post;
import com.backend.carsforum.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Post post) {
        postService.create(post);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody Post postToUpdate) {
        if (!String.valueOf(id).equals(String.valueOf(postToUpdate.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path and body do not match.");
        }
        postService.update(postToUpdate);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }

    @GetMapping("/search")
    public List<Post> searchByKeyword(@RequestParam String keyword) {
        return postService.searchByKeyword(keyword);
    }

    @GetMapping("/latest")
    public List<Post> getLatest(@RequestParam(defaultValue = "10") int limit) {
        return postService.getLatest(limit);
    }

    @GetMapping("/top")
    public List<Post> getTopCommented(@RequestParam(defaultValue = "10") int limit) {
        return postService.getTopCommented(limit);
    }
}
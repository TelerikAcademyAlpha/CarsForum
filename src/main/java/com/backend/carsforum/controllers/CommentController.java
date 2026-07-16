package com.backend.carsforum.controllers;

import com.backend.carsforum.models.Comment;
import com.backend.carsforum.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/{id}")
    public Comment getById(@PathVariable Long id) {
        return commentService.getById(id);
    }

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestParam Long authorId, @PathVariable Long postId, @Valid @RequestBody Comment comment) {
        return commentService.create(authorId, postId, comment);
    }

    @PutMapping("/comments/{id}")
    public Comment update(@PathVariable Long id, @RequestParam Long currentUserId, @RequestParam boolean isAdmin, @Valid @RequestBody Comment comment) {
        comment.setId(id);
        return commentService.update(comment, currentUserId, isAdmin);
    }

    @DeleteMapping("/comments/{id}")
    public void delete(@PathVariable Long id, @RequestParam Long currentUserId, @RequestParam boolean isAdmin) {
        commentService.delete(id, currentUserId, isAdmin);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<Comment> getByPostId(@PathVariable Long postId) {
        return commentService.getByPostId(postId);
    }

    @GetMapping("/users/{authorId}/comments")
    public List<Comment> getByAuthorId(@PathVariable Long authorId) {
        return commentService.getByAuthorId(authorId);
    }
}
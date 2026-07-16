package com.backend.carsforum.controllers;

import com.backend.carsforum.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public void likePost(@PathVariable Long postId, @RequestParam Long userId) {
        likeService.likePost(postId, userId);
    }

    @DeleteMapping
    public void unlikePost(@PathVariable Long postId, @RequestParam Long userId) {
        likeService.unlikePost(postId, userId);
    }

    @GetMapping("/check")
    public boolean hasUserLiked(@PathVariable Long postId, @RequestParam Long userId) {
        return likeService.hasUserLiked(postId, userId);
    }

    @GetMapping("/count")
    public long countLikes(@PathVariable Long postId) {
        return likeService.countLikes(postId);
    }
}
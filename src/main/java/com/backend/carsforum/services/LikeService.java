package com.backend.carsforum.services;

public interface LikeService {

    void like(Long postId);

    void unlike(Long postId);

    boolean isLiked(Long postId);

    long countLikes(Long postId);
}

package com.backend.carsforum.services;

public interface LikeService {

    void likePost(Long postId, Long userId);

    void unlikePost(Long postId, Long userId);

    boolean hasUserLiked(Long postId, Long userId);

    long countLikes(Long postId);
}

package com.backend.carsforum.repositories;

import com.backend.carsforum.models.Like;
import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.User;

import java.util.Optional;

public interface LikeRepository {

    Like save(Like like);

    void delete(Like like);

    Optional<Like> findById(Long id);

    Optional<Like> findByUserAndPost(User user, Post post);

    boolean existsByUserAndPost(User user, Post post);

    long countByPost(Post post);
}

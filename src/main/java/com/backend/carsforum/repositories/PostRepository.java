package com.backend.carsforum.repositories;

import com.backend.carsforum.models.Post;

import java.util.List;

public interface PostRepository {
    Post findById(Long id);
    void save(Post post);
    void update(Post post);
    void delete(Post post);
    List<Post> findByAuthorId(Long authorId);
    List<Post> findByKeyword(String keyword);
    List<Post> findLatest(int limit);
    List<Post> findTopCommented(int limit);
    long countAll();

}

package com.backend.forum.repositories;

import com.backend.forum.models.Post;

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
    public List<Post> findAll();

}

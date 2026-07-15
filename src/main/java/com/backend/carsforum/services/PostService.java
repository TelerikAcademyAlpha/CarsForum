package com.backend.carsforum.services;

import com.backend.carsforum.models.Post;
import java.util.List;

public interface PostService {
    Post getById(Long id);
    void create(Post post);
    void update(Post post);
    void delete(Long id);
    List<Post> getByAuthorId(Long authorId);
    List<Post> searchByKeyword(String keyword);
    List<Post> getLatest(int limit);
    List<Post> getTopCommented(int limit);
}
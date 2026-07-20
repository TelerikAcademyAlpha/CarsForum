package com.backend.forum.services;

import com.backend.forum.models.Post;
import com.backend.forum.models.dtos.request.UpdatePostRequest;

import java.util.List;

public interface PostService {
    Post create(Post post);

    Post update(Post post);

    void delete(Long id);

    Post getById(Long id);

    Post update(Long id, UpdatePostRequest request);

    List<Post> getByAuthor(Long authorId);

    List<Post> search(String keyword);

    List<Post> getLatest();

    List<Post> getTopCommented();

//    List<Post> filter(PostFilterOptions filterOptions); // later

    void addTag(Long postId, String tagName);

    void removeTag(Long postId, String tagName);

    List<Post> findAll();
}
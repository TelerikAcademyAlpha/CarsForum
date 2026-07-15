package com.backend.carsforum.services;

import com.backend.carsforum.models.Comment;

import java.util.List;

public interface CommentService {

    Comment getById(Long id);

    Comment create(Long authorId, Long postId, Comment comment);

    Comment update(Comment commentToUpdate, Long currentUserId, boolean isAdmin);

    void delete(Long id, Long currentUserId, boolean isAdmin);

    List<Comment> getByPostId(Long postId);

    List<Comment> getByAuthorId(Long authorId);
}

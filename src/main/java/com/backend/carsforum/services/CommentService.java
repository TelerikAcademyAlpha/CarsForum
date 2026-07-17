package com.backend.carsforum.services;

import com.backend.carsforum.models.Comment;

import java.util.List;

public interface CommentService {

    Comment create(Comment comment);

    Comment update(Comment comment);

    void delete(Long commentId);

    Comment getById(Long id);

    List<Comment> getByPost(Long postId);

    List<Comment> getByAuthor(Long authorId);

    Comment reply(Long parentCommentId, Comment reply);
}

package com.backend.forum.services;

import com.backend.forum.models.Comment;

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

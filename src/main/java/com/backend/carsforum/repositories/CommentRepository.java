package com.backend.carsforum.repositories;

import com.backend.carsforum.models.Comment;
import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.User;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Comment update(Comment comment);

    void delete(Comment comment);

    Optional<Comment> findById(Long id);

    List<Comment> findAll();

    List<Comment> findByPost(Post post);

    List<Comment> findByAuthor(User author);

    List<Comment> findReplies(Long parentComment);

    List<Comment> findByAuthorId(Long authorId);

    public List<Comment> findByPostId(Long postId);
}

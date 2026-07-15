package com.backend.carsforum.services;

import com.backend.carsforum.models.Comment;
import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.User;
import com.backend.carsforum.repositories.CommentRepository;
import com.backend.carsforum.repositories.CommentRepositoryImpl;
import com.backend.carsforum.repositories.PostRepository;
import com.backend.carsforum.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository)
    {
        this.commentRepository = commentRepository;
        this. postRepository = postRepository;
        this.userRepository= userRepository;
    }

    @Override
    public Comment getById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with ID " + id + " not found.");
        }
        return comment.get();
    }

    @Override
    @Transactional
    public Comment create(Long authorId, Long postId, Comment comment) {
        if (comment == null || comment.getContent() == null || comment.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment content must not be empty.");
        }
        // basic length validation (adjust if needed)
        if (comment.getContent().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment too short.");
        }

        User author = userRepository.findById(authorId);
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.");
        }

        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.");
        }

        comment.setAuthor(author);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return comment;
    }

    @Override
    @Transactional
    public Comment update(Comment commentToUpdate, Long currentUserId, boolean isAdmin) {
        if (commentToUpdate == null || commentToUpdate.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment id is required for update.");
        }
        Comment existing = getById(commentToUpdate.getId());


        Long authorId = existing.getAuthor() != null ? existing.getAuthor().getId() : null;
        if (!isAdmin && (authorId == null || !authorId.equals(currentUserId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this comment.");
        }

        // Validate new content
        if (commentToUpdate.getContent() == null || commentToUpdate.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment content must not be empty.");
        }
        if (commentToUpdate.getContent().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment too short.");
        }

        existing.setContent(commentToUpdate.getContent());


        commentRepository.update(existing);
        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id, Long currentUserId, boolean isAdmin) {
        Comment existing = getById(id);

        Long authorId = existing.getAuthor() != null ? existing.getAuthor().getId() : null;
        if (!isAdmin && (authorId == null || !authorId.equals(currentUserId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment.");
        }

        commentRepository.delete(existing);
    }

    @Override
    public List<Comment> getByPostId(Long postId) {
        if (postId == null) return List.of();
        return commentRepository.findByPostId(postId);
    }

    @Override
    public List<Comment> getByAuthorId(Long authorId) {
        if (authorId == null) return List.of();
        return commentRepository.findByAuthorId(authorId);
    }
}

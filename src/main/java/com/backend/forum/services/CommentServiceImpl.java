package com.backend.forum.services;

import com.backend.forum.exceptions.BlockedUserException;
import com.backend.forum.exceptions.EntityNotFoundException;
import com.backend.forum.exceptions.InvalidOperationException;
import com.backend.forum.models.Comment;
import com.backend.forum.models.Post;
import com.backend.forum.models.User;
import com.backend.forum.repositories.CommentRepository;
import com.backend.forum.repositories.PostRepository;
import com.backend.forum.security.AuthenticationFacade;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, AuthenticationFacade authenticationFacade, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Comment create(Comment comment) {
        User currentUser = getCurrentUser();

        if (currentUser.isBlocked()) {
            throw new BlockedUserException("Blocked users cannot comment.");
        }

        Post post = getExistingPost(comment.getPost().getId());

        comment.setAuthor(currentUser);
        comment.setPost(post);

        commentRepository.save(comment);

        return comment;
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {

        User currentUser = getCurrentUser();

        Comment existing = getExistingComment(comment.getId());

        if (!existing.getAuthor().getId().equals(currentUser.getId())) {
            throw new InvalidOperationException("You cannot edit another user's comment.");
        }
        existing.setContent(comment.getContent());

        commentRepository.update(existing);

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long commentId) {

        User currentUser = getCurrentUser();

        Comment comment = getExistingComment(commentId);

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new InvalidOperationException("You cannot delete another user's comment.");
        }

        commentRepository.delete(comment);
    }

    @Override
    public Comment getById(Long id) {
        return getExistingComment(id);
    }

    @Override
    public List<Comment> getByPost(Long postId) {

        Post post = getExistingPost(postId);

        return commentRepository.findByPost(post);
    }

    @Override
    public List<Comment> getByAuthor(Long authorId) {
        User author = userService.getById(authorId);

        return commentRepository.findByAuthor(author);
    }

    @Override
    @Transactional
    public Comment reply(Long parentCommentId, Comment reply) {

        User currentUser = getCurrentUser();

        if (currentUser.isBlocked()) {
            throw new BlockedUserException("Blocked users cannot reply.");
        }

        Comment parent = getExistingComment(parentCommentId);

        reply.setAuthor(currentUser);
        reply.setPost(parent.getPost());

        parent.addReply(reply);

        commentRepository.save(reply);

        return reply;
    }

    private Comment getExistingComment(Long id) {

        return commentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Comment",id));
    }

    private Post getExistingPost(Long id) {

        Post post = postRepository.findById(id);

        if (post == null) {
            throw new EntityNotFoundException("Post",id);
        }

        return post;
    }

    private User getCurrentUser() {

        String username = authenticationFacade.getCurrentUsername();

        return userService.getByUsername(username);
    }
}

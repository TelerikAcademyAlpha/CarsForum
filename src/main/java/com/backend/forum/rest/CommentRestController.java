package com.backend.forum.rest;

import com.backend.forum.mapper.CommentMapper;
import com.backend.forum.models.Comment;
import com.backend.forum.models.dtos.request.CreateCommentRequest;
import com.backend.forum.models.dtos.request.ReplyCommentRequest;
import com.backend.forum.models.dtos.request.UpdateCommentRequest;
import com.backend.forum.models.dtos.response.CommentResponse;
import com.backend.forum.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentRestController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(
            @Valid @RequestBody CreateCommentRequest request) {

        Comment comment = commentMapper.toEntity(request);

        return commentMapper.toResponse(
                commentService.create(comment));
    }

    @PutMapping("/{id}")
    public CommentResponse update(@PathVariable Long id,
                         @Valid @RequestBody UpdateCommentRequest request) {

        Comment comment = commentService.getById(id);

        commentMapper.updateEntity(request, comment);

        return commentMapper.toResponse(commentService.update(comment));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }

    @GetMapping("/{id}")
    public CommentResponse getById(@PathVariable Long id) {

        return commentMapper.toResponse(
                commentService.getById(id));
    }

    @GetMapping("/post/{postId}")
    public List<CommentResponse> getByPost(
            @PathVariable Long postId) {

        return commentService.getByPost(postId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @GetMapping("/author/{authorId}")
    public List<CommentResponse> getByAuthor(
            @PathVariable Long authorId) {

        return commentService.getByAuthor(authorId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @PostMapping("/{parentId}/reply")
    public CommentResponse reply(
            @PathVariable Long parentId,
            @Valid @RequestBody ReplyCommentRequest request) {

        Comment reply = commentMapper.toEntity(request);

        return commentMapper.toResponse(
                commentService.reply(parentId, reply));
    }
}

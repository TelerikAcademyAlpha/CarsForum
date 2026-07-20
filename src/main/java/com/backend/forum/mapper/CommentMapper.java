package com.backend.forum.mapper;

import com.backend.forum.models.Comment;
import com.backend.forum.models.Post;
import com.backend.forum.models.dtos.request.CreateCommentRequest;
import com.backend.forum.models.dtos.request.ReplyCommentRequest;
import com.backend.forum.models.dtos.request.UpdateCommentRequest;
import com.backend.forum.models.dtos.response.CommentResponse;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CreateCommentRequest dto) {

        Comment comment = new Comment();

        comment.setContent(dto.getContent());

        Post post = new Post();
        post.setId(dto.getPostId());

        comment.setPost(post);

        return comment;
    }

    public Comment toEntity(ReplyCommentRequest dto) {

        Comment comment = new Comment();

        comment.setContent(dto.getContent());

        return comment;
    }

    public void updateEntity(UpdateCommentRequest dto,
                             Comment comment) {

        comment.setContent(dto.getContent());
    }

    public CommentResponse toResponse(Comment comment) {

        CommentResponse response = new CommentResponse();

        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setAuthor(comment.getAuthor().getUsername());

        response.setPostId(comment.getPost().getId());

        if (comment.getParentComment() != null) {
            response.setParentCommentId(
                    comment.getParentComment().getId());
        }

        return response;
    }
}

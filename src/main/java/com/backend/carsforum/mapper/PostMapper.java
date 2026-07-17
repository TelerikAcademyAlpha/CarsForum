package com.backend.carsforum.mapper;

import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.Tag;
import com.backend.carsforum.models.dtos.request.CreatePostRequest;
import com.backend.carsforum.models.dtos.request.UpdatePostRequest;
import com.backend.carsforum.models.dtos.response.PostResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class PostMapper {
    public Post toEntity(CreatePostRequest dto) {

        Post post = new Post();

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return post;
    }

    public void updateEntity(UpdatePostRequest dto, Post post) {

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
    }

    public PostResponse toResponse(Post post) {

        PostResponse response = new PostResponse();

        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthor(post.getAuthor().getUsername());
        response.setCreatedAt(post.getCreatedAt());

        response.setLikes(post.getLikes().size());

        response.setTags(
                post.getTags()
                        .stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList())
        );

        return response;
    }
}

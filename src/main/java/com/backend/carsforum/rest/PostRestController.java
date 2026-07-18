package com.backend.carsforum.rest;

import com.backend.carsforum.mapper.PostMapper;
import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.dtos.request.CreatePostRequest;
import com.backend.carsforum.models.dtos.request.UpdatePostRequest;
import com.backend.carsforum.models.dtos.response.PostResponse;
import com.backend.carsforum.services.LikeService;
import com.backend.carsforum.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService postService;
    private final PostMapper postMapping;
    private final LikeService likeService;


    public PostRestController(PostService postService, PostMapper postMapping, LikeService likeService) {
        this.postService = postService;
        this.postMapping = postMapping;
        this.likeService = likeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody CreatePostRequest request) {

        Post post = postMapping.toEntity(request);

        return postMapping.toResponse(postService.create(post));
    }

    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id,
                       @Valid @RequestBody UpdatePostRequest request) {
        Post post = postService.getById(id);
        postMapping.updateEntity(request,post);

        return postMapping.toResponse(postService.update(post));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }

    @GetMapping("/{id}")
    public PostResponse getById(@PathVariable Long id) {
        return postMapping.toResponse( postService.getById(id));
    }

    @GetMapping
    public List<PostResponse> getAll() {
        return postService.findAll()
                .stream()
                .map(postMapping::toResponse)
                .toList();
    }

    @GetMapping("/latest")
    public List<PostResponse> latest() {

        return postService.getLatest()
                .stream()
                .map(postMapping::toResponse)
                .toList();
    }

    @GetMapping("/top-commented")
    public List<PostResponse> topCommented() {
        return postService.getTopCommented()
                .stream()
                .map(postMapping::toResponse)
                .toList();
    }

    @GetMapping("/search")
    public List<PostResponse> search(
            @RequestParam String keyword) {

        return postService.search(keyword)
                .stream()
                .map(postMapping::toResponse)
                .toList();
    }

    @PostMapping("/{id}/like")
    public void like(@PathVariable Long id) {
        likeService.like(id);
    }

    @DeleteMapping("/{id}/like")
    public void unlike(@PathVariable Long id) {
        likeService.unlike(id);
    }

    @GetMapping("/{id}/likes")
    public long likes(@PathVariable Long id) {
        return likeService.countLikes(id);
    }

    @GetMapping("/{id}/liked")
    public boolean liked(@PathVariable Long id) {
        return likeService.isLiked(id);
    }
}

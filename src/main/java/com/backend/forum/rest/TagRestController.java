package com.backend.forum.rest;

import com.backend.forum.mapper.TagMapper;
import com.backend.forum.models.dtos.request.CreateTagRequest;
import com.backend.forum.models.dtos.response.TagResponse;
import com.backend.forum.services.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    public TagRestController(TagService tagService, TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse create(
            @Valid @RequestBody CreateTagRequest request) {

        return tagMapper.toResponse(tagService.create(request.getName()));
    }

    @GetMapping("/{name}")
    public TagResponse getByName(
            @PathVariable String name) {

        return tagMapper.toResponse(tagService.getByName(name));
    }

    @GetMapping
    public List<TagResponse> getAll() {

        return tagService.findAll()
                .stream()
                .map(tagMapper::toResponse)
                .toList();
    }
}

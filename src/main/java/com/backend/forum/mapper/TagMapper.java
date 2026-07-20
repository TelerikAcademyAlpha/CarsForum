package com.backend.forum.mapper;

import com.backend.forum.models.Tag;
import com.backend.forum.models.dtos.response.TagResponse;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagResponse toResponse(Tag tag) {

        TagResponse response = new TagResponse();

        response.setId(tag.getId());
        response.setName(tag.getName());

        return response;
    }
}

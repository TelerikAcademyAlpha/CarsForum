package com.backend.carsforum.mapper;

import com.backend.carsforum.models.Tag;
import com.backend.carsforum.models.dtos.response.TagResponse;
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

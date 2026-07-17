package com.backend.carsforum.services;

import com.backend.carsforum.models.Tag;

import java.util.List;

public interface TagService {
    Tag getByName(String name);

    Tag create(String name);

    Tag getOrCreate(String name);

    List<Tag> findAll();
}

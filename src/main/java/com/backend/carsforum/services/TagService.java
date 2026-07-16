package com.backend.carsforum.services;

import com.backend.carsforum.models.Tag;

import java.util.List;

public interface TagService {
    Tag getById(Long id);

    Tag getByName(String name);

    Tag create(String name);

    Tag update(Long id, String newName);

    void delete(Long id);

    List<Tag> listAll();
}

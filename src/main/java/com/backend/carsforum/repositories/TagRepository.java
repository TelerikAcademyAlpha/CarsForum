package com.backend.carsforum.repositories;

import com.backend.carsforum.models.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    Tag save(Tag tag);

    Tag update(Tag tag);

    List<Tag> findAll();
}

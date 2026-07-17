package com.backend.carsforum.services;

import com.backend.carsforum.models.Tag;
import com.backend.carsforum.repositories.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public Tag getByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() ->
                        new IllegalArgumentException("Tag not found."));
    }

    @Override
    @jakarta.transaction.Transactional
    public Tag create(String name) {

        if (tagRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Tag already exists.");
        }

        Tag tag = new Tag();
        tag.setName(name);

        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag getOrCreate(String name) {

        return tagRepository.findByName(name)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(name.trim().toLowerCase());
                    return tagRepository.save(tag);
                });
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}

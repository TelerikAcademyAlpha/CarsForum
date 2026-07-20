package com.backend.forum.services;

import com.backend.forum.exceptions.EntityDuplicateException;
import com.backend.forum.exceptions.EntityNotFoundException;
import com.backend.forum.models.Tag;
import com.backend.forum.repositories.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public Tag getByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() ->
         new EntityNotFoundException("Tag","name",name));
    }

    @Override
    @jakarta.transaction.Transactional
    public Tag create(String name) {

        if (tagRepository.findByName(name).isPresent()) {
            throw new EntityDuplicateException("Tag", "name", name);
        }

        Tag tag = new Tag();
        tag.setName(name);

        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag getOrCreate(String name) {

        String normalized;
        if (name == null) {
            normalized = "";
        } else {
            normalized = name.trim().toLowerCase();
        }
        return tagRepository.findByName(normalized)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(name.trim().toLowerCase());
                    return tagRepository.save(tag);
                });
    }

    @Override
    @Transactional
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
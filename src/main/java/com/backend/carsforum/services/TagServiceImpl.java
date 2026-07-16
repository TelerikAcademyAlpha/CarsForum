package com.backend.carsforum.services;

import com.backend.carsforum.models.Tag;
import com.backend.carsforum.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    private String toLowerCaseName(String name){
        if (name == null)  return null;
        return name.trim().toLowerCase();
    }

    @Override
    @Transactional
    public Tag getById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag id is required.");
        }
        Optional<Tag> opt = tagRepository.findById(id);
        return opt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag with id " + id + " not found."));
    }

    @Override
    @Transactional
    public Tag getByName(String name) {
        String normalized = toLowerCaseName(name);
        if (normalized == null || normalized.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag name is required.");
        }
        Optional<Tag> opt = tagRepository.findByName(normalized);
        return opt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag '" + normalized + "' not found."));
    }


    @Override
    @Transactional
    public Tag create(String name) {
        String normalized = toLowerCaseName(name);
        if (normalized == null || normalized.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag name must not be empty.");
        }
        Optional<Tag> existing = tagRepository.findByName(normalized);
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag '" + normalized + "' already exists.");
        }
        Tag tag = new Tag();
        tag.setName(normalized);

        Tag saved = tagRepository.save(tag);
        return saved;
    }

    @Override
    @Transactional
    public Tag update(Long id, String newName) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag id is required.");
        }
        String normalized = toLowerCaseName(newName);
        if (normalized == null || normalized.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New tag name must not be empty.");
        }

        Tag existing = getById(id);

        Optional<Tag> withSameName = tagRepository.findByName(normalized);
        if (withSameName.isPresent() && !withSameName.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Another tag with name '" + normalized + "' already exists.");
        }

        existing.setName(normalized);
        Tag updated = tagRepository.update(existing);
        return updated;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag id is required.");
        }
        Optional<Tag> opt = tagRepository.findById(id);
        Tag tag = opt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag with id " + id + " not found."));

        tagRepository.delete(tag);
    }

    @Override
    @Transactional
    public List<Tag> listAll() {
        return tagRepository.findAll();
    }
}

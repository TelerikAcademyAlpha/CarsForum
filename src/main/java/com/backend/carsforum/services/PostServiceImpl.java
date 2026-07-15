package com.backend.carsforum.services;

import com.backend.carsforum.models.Post;
import com.backend.carsforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post getById(Long id) {
        Post post = postRepository.findById(id);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with ID " + id + " not found.");
        }
        return post;
    }

    @Override
    @Transactional
    public void create(Post post) {
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void update(Post postToUpdate) {
        getById(postToUpdate.getId());
        postRepository.update(postToUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Post postToDelete = getById(id);
        postRepository.delete(postToDelete);
    }

    @Override
    public List<Post> getByAuthorId(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Post> searchByKeyword(String keyword) {
        return postRepository.findByKeyword(keyword);
    }

    @Override
    public List<Post> getLatest(int limit) {
        return postRepository.findLatest(limit);
    }

    @Override
    public List<Post> getTopCommented(int limit) {
        return postRepository.findTopCommented(limit);
    }
}
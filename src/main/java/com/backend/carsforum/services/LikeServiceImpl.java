package com.backend.carsforum.services;

import com.backend.carsforum.models.Like;
import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.User;
import com.backend.carsforum.repositories.LikeRepository;
import com.backend.carsforum.repositories.PostRepository;
import com.backend.carsforum.repositories.UserRepository;
import com.backend.carsforum.repositories.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public void likePost(Long postId, Long userId) {
        if (postId == null || userId == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "postId and userId are required.");
        }
        User user = userRepository.findById(userId);
        if (user == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.");
        }
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already liked this post.");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        if (postId == null || userId == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "postId and userId are required.");
        }
        User user = userRepository.findById(userId);
        if (user == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.");
        }
        Optional<Like> maybe = likeRepository.findByUserAndPost(user, post);
        if (maybe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have not liked this post.");
        }
        Like like = maybe.get();
        likeRepository.delete(like);

    }

    @Override
    @Transactional
    public boolean hasUserLiked(Long postId, Long userId) {
        if (postId == null || userId == null) return false;
        User user = userRepository.findById(userId);
        Post post = postRepository.findById(postId);
        if (user == null || post == null) return false;
        return likeRepository.existsByUserAndPost(user, post);
    }

    @Override
    @Transactional
    public long countLikes(Long postId) {
        if (postId == null) return 0L;
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.");
        }
        return likeRepository.countByPost(post);
    }
}

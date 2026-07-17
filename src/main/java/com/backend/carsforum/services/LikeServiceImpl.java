package com.backend.carsforum.services;

import com.backend.carsforum.models.Like;
import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.User;
import com.backend.carsforum.repositories.LikeRepository;
import com.backend.carsforum.repositories.PostRepository;
import com.backend.carsforum.repositories.UserRepository;
import com.backend.carsforum.repositories.UserRepositoryImpl;
import com.backend.carsforum.security.AuthenticationFacade;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;

    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, AuthenticationFacade authenticationFacade, UserService userService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
    }


    @Override
    @jakarta.transaction.Transactional
    public void like(Long postId) {

        User currentUser = getCurrentUser();

        if (currentUser.isBlocked()) {
            throw new IllegalArgumentException("Blocked users cannot like posts.");
        }

        Post post = getExistingPost(postId);

        if (likeRepository.existsByUserAndPost(currentUser, post)) {
            throw new IllegalArgumentException("You already liked this post.");
        }

        Like like = new Like();
        like.setUser(currentUser);
        like.setPost(post);

        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void unlike(Long postId) {

        User currentUser = getCurrentUser();

        Post post = getExistingPost(postId);

        Like like = likeRepository.findByUserAndPost(currentUser, post)
                .orElseThrow(() ->
                        new IllegalArgumentException("You have not liked this post."));

        likeRepository.delete(like);
    }

    @Override
    public boolean isLiked(Long postId) {

        User currentUser = getCurrentUser();

        Post post = getExistingPost(postId);

        return likeRepository.existsByUserAndPost(currentUser, post);
    }

    @Override
    public long countLikes(Long postId) {
        Post post = getExistingPost(postId);
        return likeRepository.countByPost(post);
    }

    private User getCurrentUser() {

        String username = authenticationFacade.getCurrentUsername();

        return userService.getByUsername(username);
    }

    private Post getExistingPost(Long id) {

        Post post = postRepository.findById(id);

        if (post == null) {
            throw new IllegalArgumentException("Post not found.");
        }

        return post;
    }
}

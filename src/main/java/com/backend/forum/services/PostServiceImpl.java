package com.backend.forum.services;

import com.backend.forum.exceptions.BlockedUserException;
import com.backend.forum.exceptions.EntityNotFoundException;
import com.backend.forum.exceptions.InvalidOperationException;
import com.backend.forum.models.Post;
import com.backend.forum.models.Tag;
import com.backend.forum.models.User;
import com.backend.forum.repositories.PostRepository;
import com.backend.forum.security.AuthenticationFacade;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final TagService tagService;

    public PostServiceImpl(PostRepository postRepository, AuthenticationFacade authenticationFacade, UserService userService, TagService tagService) {
        this.postRepository = postRepository;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.tagService = tagService;
    }

    @Override
    @jakarta.transaction.Transactional
    public Post create(Post post) {

        User author = getCurrentUser();

        if (author.isBlocked()){
            throw new BlockedUserException("Blocked users cannot create posts.");
        }

        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

        return post;
    }

    @Override
    @jakarta.transaction.Transactional
    public Post update(Post post) {
        Post existing = getExistingPost(post.getId());

        String username = authenticationFacade.getCurrentUsername();
        User currentUser = userService.getByUsername(username);

        if (!existing.getAuthor().getId().equals(currentUser.getId())) {
            throw new InvalidOperationException("You cannot edit another user's post.");
        }

        existing.setTitle(post.getTitle());
        existing.setContent(post.getContent());

        postRepository.update(existing);

        return existing;    }

    @Override
    @jakarta.transaction.Transactional
    public void delete(Long id) {

        Post post = getExistingPost(id);

        String username = authenticationFacade.getCurrentUsername();
        User currentUser = userService.getByUsername(username);

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new InvalidOperationException("You cannot delete another user's post.");
        }

        postRepository.delete(post);
    }

    @Override
    public Post getById(Long id) {
        return getExistingPost(id);
    }

    @Override
    public List<Post> getByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Post> search(String keyword) {
        return postRepository.findByKeyword(keyword);
    }

    @Override
    public List<Post> getLatest() {
        return postRepository.findLatest(10);
    }

    @Override
    public List<Post> getTopCommented() {
        return postRepository.findTopCommented(10);
    }

    @Override
    @jakarta.transaction.Transactional
    public void addTag(Long postId, String tagName) {

        Post post = getExistingPost(postId);

        Tag tag = tagService.getOrCreate(tagName);

        post.addTag(tag);

        postRepository.update(post);
    }

    @Override
    @Transactional
    public void removeTag(Long postId, String tagName) {

        Post post = getExistingPost(postId);

        Tag tag = tagService.getByName(tagName);

        post.removeTag(tag);

        postRepository.update(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    private Post getExistingPost(Long id) {
        Post post = postRepository.findById(id);

        if (post == null) {
            throw new EntityNotFoundException("Post",id);
        }

        return post;
    }

    private User getCurrentUser() {

        String username = authenticationFacade.getCurrentUsername();

        return userService.getByUsername(username);
    }

}
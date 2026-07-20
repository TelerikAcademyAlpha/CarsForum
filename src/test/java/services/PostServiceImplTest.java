package services;

import com.backend.forum.exceptions.BlockedUserException;
import com.backend.forum.exceptions.InvalidOperationException;
import com.backend.forum.exceptions.EntityNotFoundException;
import com.backend.forum.models.Post;
import com.backend.forum.models.Tag;
import com.backend.forum.models.User;
import com.backend.forum.repositories.PostRepository;
import com.backend.forum.security.AuthenticationFacade;
import com.backend.forum.services.PostServiceImpl;
import com.backend.forum.services.TagService;
import com.backend.forum.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserService userService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostServiceImpl postService;

    private User author;
    private Post existingPost;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);
        author.setUsername("ivanp");
        author.setFirstName("Ivan");
        author.setLastName("Petrov");

        existingPost = new Post();
        existingPost.setId(10L);
        existingPost.setTitle("Sample title");
        existingPost.setContent("Sample content");
        existingPost.setAuthor(author);
        existingPost.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void create_success() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("ivanp");
        when(userService.getByUsername("ivanp")).thenReturn(author);
        doNothing().when(postRepository).save(any(Post.class));

        Post toCreate = new Post();
        toCreate.setTitle("New post title");
        toCreate.setContent("Some content");

        Post created = postService.create(toCreate);

        assertNotNull(created.getAuthor());
        assertEquals(author, created.getAuthor());
        assertNotNull(created.getCreatedAt());
        verify(postRepository).save(toCreate);
    }

    @Test
    void create_blockedUser_throws() {
        author.setBlocked(true);
        when(authenticationFacade.getCurrentUsername()).thenReturn("ivanp");
        when(userService.getByUsername("ivanp")).thenReturn(author);

        Post toCreate = new Post();
        toCreate.setTitle("Title");
        toCreate.setContent("Content");

        assertThrows(BlockedUserException.class, () -> postService.create(toCreate));
        verify(postRepository, never()).save(any());
    }

    @Test
    void update_success() {
        when(postRepository.findById(10L)).thenReturn(existingPost);
        when(authenticationFacade.getCurrentUsername()).thenReturn("ivanp");
        when(userService.getByUsername("ivanp")).thenReturn(author);
        doNothing().when(postRepository).update(existingPost);

        Post incoming = new Post();
        incoming.setId(10L);
        incoming.setTitle("Updated title");
        incoming.setContent("Updated content");

        Post updated = postService.update(incoming);

        assertEquals("Updated title", updated.getTitle());
        assertEquals("Updated content", updated.getContent());
        verify(postRepository).update(existingPost);
    }

    @Test
    void update_editOtherUsersPost_throws() {
        User other = new User();
        other.setId(99L);
        existingPost.setAuthor(other);

        when(postRepository.findById(10L)).thenReturn(existingPost);
        when(authenticationFacade.getCurrentUsername()).thenReturn("ivanp");
        when(userService.getByUsername("ivanp")).thenReturn(author);

        Post incoming = new Post();
        incoming.setId(10L);
        incoming.setTitle("Updated title");
        incoming.setContent("Updated content");

        assertThrows(InvalidOperationException.class, () -> postService.update(incoming));
        verify(postRepository, never()).update(any());
    }

    @Test
    void delete_success() {
        when(postRepository.findById(10L)).thenReturn(existingPost);
        when(authenticationFacade.getCurrentUsername()).thenReturn("ivanp");
        when(userService.getByUsername("ivanp")).thenReturn(author);
        doNothing().when(postRepository).delete(existingPost);

        postService.delete(10L);

        verify(postRepository).delete(existingPost);
    }

    @Test
    void delete_deleteOtherUsersPost_throws() {
        User other = new User();
        other.setId(999L);
        existingPost.setAuthor(other);

        when(postRepository.findById(10L)).thenReturn(existingPost);
        when(authenticationFacade.getCurrentUsername()).thenReturn("ivanp");
        when(userService.getByUsername("ivanp")).thenReturn(author);

        assertThrows(InvalidOperationException.class, () -> postService.delete(10L));
        verify(postRepository, never()).delete(any());
    }

    @Test
    void getById_found() {
        when(postRepository.findById(10L)).thenReturn(existingPost);
        Post p = postService.getById(10L);
        assertEquals(existingPost, p);
    }

    @Test
    void getById_notFound_throws() {
        when(postRepository.findById(999L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> postService.getById(999L));
    }

    @Test
    void getByAuthor_search_latest_topCommented_findAll() {
        List<Post> some = List.of(existingPost);
        when(postRepository.findByAuthorId(1L)).thenReturn(some);
        when(postRepository.findByKeyword("x")).thenReturn(some);
        when(postRepository.findLatest(10)).thenReturn(some);
        when(postRepository.findTopCommented(10)).thenReturn(some);
        when(postRepository.findAll()).thenReturn(some);

        assertEquals(some, postService.getByAuthor(1L));
        assertEquals(some, postService.search("x"));
        assertEquals(some, postService.getLatest());
        assertEquals(some, postService.getTopCommented());
        assertEquals(some, postService.findAll());
    }

    @Test
    void addTag_success() {
        when(postRepository.findById(10L)).thenReturn(existingPost);
        Tag tag = new Tag();
        tag.setId(5L);
        tag.setName("cardio");
        when(tagService.getOrCreate("cardio")).thenReturn(tag);
        doNothing().when(postRepository).update(existingPost);

        postService.addTag(10L, "cardio");

        assertTrue(existingPost.getTags().contains(tag));
        verify(postRepository).update(existingPost);
    }

    @Test
    void removeTag_success() {
        Tag tag = new Tag();
        tag.setId(5L);
        tag.setName("cardio");
        existingPost.getTags().add(tag);

        when(postRepository.findById(10L)).thenReturn(existingPost);
        when(tagService.getByName("cardio")).thenReturn(tag);
        doNothing().when(postRepository).update(existingPost);

        postService.removeTag(10L, "cardio");
        assertFalse(existingPost.getTags().contains(tag));
        verify(postRepository).update(existingPost);
    }
}

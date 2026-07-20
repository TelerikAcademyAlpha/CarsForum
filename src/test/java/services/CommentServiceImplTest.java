package services;

import com.backend.forum.exceptions.BlockedUserException;
import com.backend.forum.exceptions.EntityNotFoundException;
import com.backend.forum.exceptions.InvalidOperationException;
import com.backend.forum.models.Comment;
import com.backend.forum.models.Post;
import com.backend.forum.models.User;
import com.backend.forum.repositories.CommentRepository;
import com.backend.forum.repositories.PostRepository;
import com.backend.forum.security.AuthenticationFacade;
import com.backend.forum.services.CommentServiceImpl;
import com.backend.forum.services.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    AuthenticationFacade authenticationFacade;
    @Mock
    UserService userService;

    CommentServiceImpl commentService;

    @BeforeEach
    void setUp(){
        commentService = new CommentServiceImpl(commentRepository, postRepository, authenticationFacade, userService);
    }

    private User testUser(long id, boolean blocked) {
        User u = new User();
        u.setId(id);
        u.setBlocked(blocked);
        u.setUsername("user" + id);
        return u;
    }

    private Post testPost(long id) {
        Post p = new Post();
        p.setId(id);
        return p;
    }

    private Comment testComment(long id, User author, Post post) {
        Comment c = new Comment();
        c.setId(id);
        c.setAuthor(author);
        c.setPost(post);
        c.setContent("orig");
        return c;
    }

    @Test
    void create_success() {
        User current = new User();
        current.setId(1L);
        current.setUsername("user1");
        current.setBlocked(false);

        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Post post = new Post();
        post.setId(10L);
        when(postRepository.findById(10L)).thenReturn(post);

        Comment input = new Comment();
        Post postRef = new Post();
        postRef.setId(10L);
        input.setPost(postRef);
        input.setContent("hi");

        Comment saved = commentService.create(input);

        verify(commentRepository, times(1)).save(any(Comment.class));
        assertEquals(current, saved.getAuthor());
        assertEquals(post, saved.getPost());
        assertEquals("hi", saved.getContent());
    }

    @Test
    void create_blockedUser_throws() {
        User blocked = testUser(2L, true);
        when(authenticationFacade.getCurrentUsername()).thenReturn(blocked.getUsername());
        when(userService.getByUsername(blocked.getUsername())).thenReturn(blocked);

        Comment input = new Comment();
        Post postRef = new Post(); postRef.setId(5L);
        input.setPost(postRef);

        BlockedUserException ex = assertThrows(BlockedUserException.class, () -> commentService.create(input));
        assertEquals("Blocked users cannot comment.", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void create_postNotFound_throws() {
        User current = testUser(3L, false);
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);
        when(postRepository.findById(99L)).thenReturn(null);

        Comment input = new Comment();
        Post p = new Post();
        p.setId(99L);
        input.setPost(p);
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> commentService.create(input));
        assertEquals("Post with id 99 not found.", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void update_success_whenAuthorMatches() {
        User current = testUser(4L, false);
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Post post = testPost(20L);
        Comment existing = testComment(100L, current, post);
        when(commentRepository.findById(100L)).thenReturn(Optional.of(existing));

        Comment incoming = new Comment();
        incoming.setId(100L);
        incoming.setContent("updated content");

        Comment updated = commentService.update(incoming);
        verify(commentRepository, times(1)).update(existing);
        assertEquals("updated content", updated.getContent());
    }

    @Test
    void update_notAuthor_throws() {
        User author = testUser(5L, false);
        User current = testUser(6L, false);
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Comment existing = testComment(200L, author, testPost(30L));
        when(commentRepository.findById(200L)).thenReturn(java.util.Optional.of(existing));
        Comment incoming = new Comment();
        incoming.setId(200L);
        incoming.setContent("x");

        InvalidOperationException ex = assertThrows(InvalidOperationException.class, () -> commentService.update(incoming));
        assertEquals("You cannot edit another user's comment.", ex.getMessage());
        verify(commentRepository, never()).update(any());
    }

    @Test
    void delete_success_whenAuthorMatches() {
        User current = testUser(7L, false);
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Comment existing = testComment(300L, current, testPost(40L));
        when(commentRepository.findById(300L)).thenReturn(Optional.of(existing));

        commentService.delete(300L);

        verify(commentRepository, times(1)).delete(existing);
    }

    @Test
    void delete_notAuthor_throws() {
        User author = testUser(8L, false);
        User current = testUser(9L, false);
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Comment existing = testComment(400L, author, testPost(50L));
        when(commentRepository.findById(400L)).thenReturn(Optional.of(existing));

        InvalidOperationException ex = assertThrows(InvalidOperationException.class, () -> commentService.delete(400L));
        assertEquals("You cannot delete another user's comment.", ex.getMessage());
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void reply_success() {
        User current = testUser(10L, false);
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Post post = testPost(60L);
        Comment parent = testComment(500L, testUser(11L, false), post);
        when(commentRepository.findById(500L)).thenReturn(Optional.of(parent));

        Comment reply = new Comment();
        reply.setContent("reply text");

        Comment saved = commentService.reply(500L, reply);

        verify(commentRepository, times(1)).save(reply);
        assertEquals(current, saved.getAuthor());
        assertEquals(post, saved.getPost());
    }

    @Test
    void getByPost_returnsList() {
        Post post = testPost(70L);
        when(postRepository.findById(70L)).thenReturn(post);

        Comment c1 = testComment(1L, testUser(12L, false), post);
        Comment c2 = testComment(2L, testUser(13L, false), post);
        when(commentRepository.findByPost(post)).thenReturn(List.of(c1, c2));

        List<Comment> result = commentService.getByPost(70L);
        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
    }

    @Test
    void getByAuthor_callsRepository() {
        User author = testUser(20L, false);
        when(userService.getById(20L)).thenReturn(author);

        Comment c = testComment(10L, author, testPost(81L));
        when(commentRepository.findByAuthor(author)).thenReturn(List.of(c));

        List<Comment> result = commentService.getByAuthor(20L);
        assertEquals(1, result.size());
        assertEquals(c, result.get(0));
    }

}

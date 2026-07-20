package services;

import com.backend.forum.exceptions.EntityNotFoundException;
import com.backend.forum.models.Like;
import com.backend.forum.models.Post;
import com.backend.forum.models.User;
import com.backend.forum.repositories.LikeRepository;
import com.backend.forum.repositories.PostRepository;
import com.backend.forum.security.AuthenticationFacade;
import com.backend.forum.services.LikeServiceImpl;
import com.backend.forum.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceImplTest {
    @Mock
    LikeRepository likeRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    AuthenticationFacade authenticationFacade;
    @Mock
    UserService userService;

    LikeServiceImpl likeService;

    @BeforeEach
    void setUp() {
        likeService = new LikeServiceImpl(likeRepository, postRepository, authenticationFacade, userService);
    }

    private User testUser(long id, boolean blocked, String username) {
        User u = new User();
        u.setId(id);
        u.setBlocked(blocked);
        u.setUsername(username);
        return u;
    }

    private Post testPost(long id) {
        Post p = new Post();
        p.setId(id);
        return p;
    }

    @Test
    void like_success() {
        User current = testUser(1L, false, "u1");
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Post post = testPost(10L);
        when(postRepository.findById(10L)).thenReturn(post);
        when(likeRepository.existsByUserAndPost(current, post)).thenReturn(false);

        likeService.like(10L);

        ArgumentCaptor<Like> captor = ArgumentCaptor.forClass(Like.class);
        verify(likeRepository, times(1)).save(captor.capture());
        Like saved = captor.getValue();
        assertEquals(current, saved.getUser());
        assertEquals(post, saved.getPost());
    }

    @Test
    void like_alreadyLiked_throws() {
        User current = testUser(2L, false, "u2");
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Post post = testPost(11L);
        when(postRepository.findById(11L)).thenReturn(post);
        when(likeRepository.existsByUserAndPost(current, post)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> likeService.like(11L));
        assertTrue(ex.getMessage().contains("already liked"));
        verify(likeRepository, never()).save(any());
    }

    @Test
    void like_blockedUser_throws() {
        User blocked = testUser(3L, true, "blockedUser");
        when(authenticationFacade.getCurrentUsername()).thenReturn(blocked.getUsername());
        when(userService.getByUsername(blocked.getUsername())).thenReturn(blocked);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> likeService.like(5L));
        assertTrue(ex.getMessage().toLowerCase().contains("blocked"));
        verify(likeRepository, never()).save(any());
    }

    @Test
    void like_postNotFound_throws() {
        User current = testUser(4L, false, "u4");
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        when(postRepository.findById(99L)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> likeService.like(99L));
        assertTrue(ex.getMessage().toLowerCase().contains("post"));
        verify(likeRepository, never()).save(any());
    }

    @Test
    void unlike_success() {
        User current = testUser(6L, false, "u6");
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Post post = testPost(20L);
        when(postRepository.findById(20L)).thenReturn(post);

        Like like = new Like();
        like.setId(100L);
        like.setUser(current);
        like.setPost(post);
        when(likeRepository.findByUserAndPost(current, post)).thenReturn(Optional.of(like));

        likeService.unlike(20L);

        verify(likeRepository, times(1)).delete(like);
    }

    @Test
    void unlike_notLiked_throws() {
        User current = testUser(7L, false, "u7");
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Post post = testPost(21L);
        when(postRepository.findById(21L)).thenReturn(post);

        when(likeRepository.findByUserAndPost(current, post)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> likeService.unlike(21L));
        assertTrue(ex.getMessage().toLowerCase().contains("have not liked"));
        verify(likeRepository, never()).delete(any());
    }

    @Test
    void isLiked_true_and_false() {
        User current = testUser(8L, false, "u8");
        when(authenticationFacade.getCurrentUsername()).thenReturn(current.getUsername());
        when(userService.getByUsername(current.getUsername())).thenReturn(current);

        Post post = testPost(30L);
        when(postRepository.findById(30L)).thenReturn(post);

        when(likeRepository.existsByUserAndPost(current, post)).thenReturn(true);
        assertTrue(likeService.isLiked(30L));

        when(likeRepository.existsByUserAndPost(current, post)).thenReturn(false);
        assertFalse(likeService.isLiked(30L));
    }

    @Test
    void countLikes_delegates_and_postNotFound_throws() {
        Post post = testPost(40L);
        when(postRepository.findById(40L)).thenReturn(post);
        when(likeRepository.countByPost(post)).thenReturn(5L);
        long cnt = likeService.countLikes(40L);
        assertEquals(5L, cnt);

        when(postRepository.findById(999L)).thenReturn(null);
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> likeService.countLikes(999L));
        assertTrue(ex.getMessage().toLowerCase().contains("post"));
    }

}

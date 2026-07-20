package services;

import com.backend.forum.exceptions.EntityDuplicateException;
import com.backend.forum.exceptions.EntityNotFoundException;
import com.backend.forum.mapper.UserMapper;
import com.backend.forum.models.Role;
import com.backend.forum.models.User;
import com.backend.forum.models.dtos.request.UpdateUserRequest;
import com.backend.forum.repositories.UserRepository;
import com.backend.forum.security.AuthenticationFacade;
import com.backend.forum.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setFirstName("Ivan");
        sampleUser.setLastName("Petrov");
        sampleUser.setEmail("ivan@example.com");
        sampleUser.setUsername("ivanp");
        sampleUser.setPassword("rawpass");
        sampleUser.setRole(Role.USER);
        sampleUser.setBlocked(false);
    }

    @Test
    void register_success() {
        when(userRepository.existsByUsername("ivanp")).thenReturn(false);
        when(userRepository.existsByEmail("ivan@example.com")).thenReturn(false);
        when(passwordEncoder.encode("rawpass")).thenReturn("encoded-pass");

        doNothing().when(userRepository).save(any(User.class));

        User toRegister = new User();
        toRegister.setUsername("ivanp");
        toRegister.setEmail("ivan@example.com");
        toRegister.setPassword("rawpass");
        toRegister.setFirstName("Ivan");
        toRegister.setLastName("Petrov");

        User created = userService.register(toRegister);

        assertNotNull(created);
        assertEquals(Role.USER, created.getRole());
        assertEquals("encoded-pass", created.getPassword());
        verify(userRepository).save(toRegister);
    }

    @Test
    void register_duplicateUsername_throws() {
        when(userRepository.existsByUsername("ivanp")).thenReturn(true);

        User toRegister = new User();
        toRegister.setUsername("ivanp");
        toRegister.setEmail("ivan@example.com");
        toRegister.setPassword("rawpass");

        EntityDuplicateException ex = assertThrows(EntityDuplicateException.class,
                () -> userService.register(toRegister));
        assertTrue(ex.getMessage().toLowerCase().contains("username"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_duplicateEmail_throws() {
        when(userRepository.existsByUsername("ivanp")).thenReturn(false);
        when(userRepository.existsByEmail("ivan@example.com")).thenReturn(true);

        User toRegister = new User();
        toRegister.setUsername("ivanp");
        toRegister.setEmail("ivan@example.com");
        toRegister.setPassword("rawpass");

        EntityDuplicateException ex = assertThrows(EntityDuplicateException.class,
                () -> userService.register(toRegister));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getById_existing_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(sampleUser);

        User found = userService.getById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void getById_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> userService.getById(99L));
    }

    @Test
    void getByUsername_notFound_throws() {
        when(userRepository.findByUsername("noone")).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> userService.getByUsername("noone"));
    }

    @Test
    void getByEmail_notFound_throws() {
        when(userRepository.findByEmail("noone@example.com")).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> userService.getByEmail("noone@example.com"));
    }

    @Test
    void updateProfile_callsMapperAndUpdate() {
        UpdateUserRequest req = mock(UpdateUserRequest.class);
        when(authenticationFacade.getCurrentUsername()).thenReturn("ivanp");
        when(userRepository.findByUsername("ivanp")).thenReturn(sampleUser);

        doNothing().when(userMapper).updateEntity(req, sampleUser);
        doNothing().when(userRepository).update(sampleUser);

        User result = userService.updateProfile(req);

        verify(userMapper).updateEntity(req, sampleUser);
        verify(userRepository).update(sampleUser);
        assertEquals(sampleUser, result);
    }

    @Test
    void update_duplicateEmail_throws() {
        User updated = new User();
        updated.setId(1L);
        updated.setEmail("new@example.com");

        User existing = new User();
        existing.setId(1L);
        existing.setEmail("old@example.com");

        when(userRepository.findById(1L)).thenReturn(existing);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);

        assertThrows(EntityDuplicateException.class, () -> userService.update(updated));
        verify(userRepository, never()).update(existing);
    }

    @Test
    void update_success_updatesFieldsAndCallsRepo() {
        User updated = new User();
        updated.setId(1L);
        updated.setFirstName("NewName");
        updated.setLastName("NewLast");
        updated.setEmail("new@example.com");

        User existing = new User();
        existing.setId(1L);
        existing.setFirstName("Old");
        existing.setLastName("Old");
        existing.setEmail("old@example.com");

        when(userRepository.findById(1L)).thenReturn(existing);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        doNothing().when(userRepository).update(existing);

        User result = userService.update(updated);

        assertEquals("NewName", result.getFirstName());
        assertEquals("NewLast", result.getLastName());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).update(existing);
    }

    @Test
    void block_unblock_promote_delete_behaviour() {
        User existing = new User();
        existing.setId(5L);
        existing.setBlocked(false);
        existing.setRole(Role.USER);

        when(userRepository.findById(5L)).thenReturn(existing);
        doNothing().when(userRepository).update(existing);
        doNothing().when(userRepository).delete(existing);

        userService.block(5L);
        assertTrue(existing.isBlocked());
        verify(userRepository).update(existing);

        userService.unblock(5L);
        assertFalse(existing.isBlocked());
        verify(userRepository, times(2)).update(existing);

        userService.promoteToAdmin(5L);
        assertEquals(Role.ADMIN, existing.getRole());
        verify(userRepository, times(3)).update(existing);

        userService.delete(5L);
        verify(userRepository).delete(existing);
    }

    @Test
    void getUsersCount_and_getAll_and_getCurrentUser() {
        when(userRepository.countAll()).thenReturn(7L);
        long cnt = userService.getUsersCount();
        assertEquals(7L, cnt);

        List<User> list = List.of(sampleUser);
        when(userRepository.findAll()).thenReturn(list);
        assertEquals(list, userService.getAll());

        when(authenticationFacade.getCurrentUsername()).thenReturn("ivanp");
        when(userRepository.findByUsername("ivanp")).thenReturn(sampleUser);
        assertEquals(sampleUser, userService.getCurrentUser());
    }

    @Test
    void delete_nonExisting_throws() {
        when(userRepository.findById(99L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> userService.delete(99L));
    }
}

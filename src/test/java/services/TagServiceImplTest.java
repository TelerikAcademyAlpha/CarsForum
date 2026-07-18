package services;

import com.backend.carsforum.exceptions.EntityDuplicateException;
import com.backend.carsforum.exceptions.EntityNotFoundException;
import com.backend.carsforum.models.Tag;
import com.backend.carsforum.repositories.TagRepository;
import com.backend.carsforum.services.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {
    @Mock
    TagRepository tagRepository;
    TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagRepository);
    }

    @Test
    void getByName_success() {
        Tag t = new Tag();
        t.setId(1L);
        t.setName("java");
        when(tagRepository.findByName("java")).thenReturn(Optional.of(t));
        Tag res = tagService.getByName("java");
        assertEquals(t, res);
    }

    @Test
    void getByName_notFound_throws() {
        when(tagRepository.findByName("nope")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> tagService.getByName("nope"));
        assertTrue(ex.getMessage().toLowerCase().contains("tag"));
    }

    @Test
    void create_success() {
        when(tagRepository.findByName("spring")).thenReturn(Optional.empty());
        Tag saved = new Tag();
        saved.setId(5L);
        saved.setName("spring");
        when(tagRepository.save(any(Tag.class))).thenReturn(saved);

        Tag res = tagService.create("spring");
        assertEquals(saved, res);
        ArgumentCaptor<Tag> cap = ArgumentCaptor.forClass(Tag.class);
        verify(tagRepository).save(cap.capture());
        assertEquals("spring", cap.getValue().getName());
    }

    @Test
    void create_duplicate_throws() {
        Tag existing = new Tag();
        existing.setId(2L);
        existing.setName("react");
        when(tagRepository.findByName("react")).thenReturn(Optional.of(existing));

        EntityDuplicateException ex = assertThrows(EntityDuplicateException.class, () -> tagService.create("react"));
        assertTrue(ex.getMessage().toLowerCase().contains("tag"));
        verify(tagRepository, never()).save(any());
    }

    @Test
    void getOrCreate_existing_returnsExisting() {
        Tag existing = new Tag();
        existing.setId(7L);
        existing.setName("docker");
        when(tagRepository.findByName("docker")).thenReturn(Optional.of(existing));

        Tag res = tagService.getOrCreate("docker");
        assertEquals(existing, res);
        verify(tagRepository, never()).save(any());
    }

    @Test
    void getOrCreate_createsWhenMissing() {
        when(tagRepository.findByName("k8s")).thenReturn(Optional.empty());
        Tag saved = new Tag();
        saved.setId(9L);
        saved.setName("k8s");
        when(tagRepository.save(any(Tag.class))).thenReturn(saved);

        Tag res = tagService.getOrCreate(" K8S ");
        assertEquals(saved, res);

        ArgumentCaptor<Tag> cap = ArgumentCaptor.forClass(Tag.class);
        verify(tagRepository).save(cap.capture());
        assertEquals("k8s", cap.getValue().getName());
    }

    @Test
    void findAll_delegates() {
        Tag a = new Tag(); a.setName("a");
        Tag b = new Tag(); b.setName("b");
        when(tagRepository.findAll()).thenReturn(List.of(a,b));
        List<Tag> res = tagService.findAll();
        assertEquals(2, res.size());
        assertTrue(res.contains(a));
        assertTrue(res.contains(b));
    }
}

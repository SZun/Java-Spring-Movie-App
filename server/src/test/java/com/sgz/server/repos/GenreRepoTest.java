package com.sgz.server.repos;

import com.sgz.server.entities.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GenreRepoTest {

    @Mock
    private GenreRepo toTest;

    private final UUID id = new UUID(36, 36);

    private final Genre expectedGenre = new Genre(this.id, "Horror");

    @Test
    void save() {
        given(toTest.save(any(Genre.class))).willReturn(expectedGenre);

        ArgumentCaptor<Genre> captor = ArgumentCaptor.forClass(Genre.class);

        Genre fromRepo = toTest.save(expectedGenre);

        verify(toTest).save(captor.capture());

        Genre expectedParam = captor.getValue();
        assertEquals(id, expectedParam.getId());
        assertEquals("Horror", expectedParam.getName());

        assertEquals(expectedGenre, fromRepo);
    }

    @Test
    void findById() {
        given(toTest.findById(any(UUID.class))).willReturn(Optional.of(expectedGenre));

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Genre> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedGenre, fromRepo.get());
    }

    @Test
    void existsById() {
        given(toTest.existsById(any(UUID.class))).willReturn(true);

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        boolean fromRepo = toTest.existsById(id);

        verify(toTest).existsById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo);
    }

    @Test
    void existsByName() {
        given(toTest.existsByName(anyString())).willReturn(true);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        boolean fromRepo = toTest.existsByName("Horror");

        verify(toTest).existsByName(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals("Horror", expectedParam);
        assertTrue(fromRepo);
    }

    @Test
    void findByIdEmpty() {
        given(toTest.findById(any(UUID.class))).willReturn(Optional.empty());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Genre> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isEmpty());
    }

    @Test
    void deleteById() {
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        toTest.deleteById(id);

        verify(toTest).deleteById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
    }

    @Test
    void findAll() {
        final List<Genre> expectedGenres = Arrays.asList(expectedGenre);

        given(toTest.findAll()).willReturn(expectedGenres);

        List<Genre> fromRepo = toTest.findAll();

        verify(toTest).findAll();

        assertEquals(expectedGenres, fromRepo);
    }

}
package com.sgz.server.repos;

import com.sgz.server.entities.Genre;
import com.sgz.server.entities.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MovieRepoTest {

    @Mock
    private MovieRepo toTest;

    private final UUID id = new UUID(36, 36);

    private final Genre testGenre = new Genre(this.id, "Horror");

    private final Movie expectedMovie = new Movie(this.id, "Disaster Artist", this.testGenre, 100, new BigDecimal("1.00"));

    @Test
    void save() {
        given(toTest.save(any(Movie.class))).willReturn(expectedMovie);

        ArgumentCaptor<Movie> captor = ArgumentCaptor.forClass(Movie.class);

        Movie fromRepo = toTest.save(expectedMovie);

        verify(toTest).save(captor.capture());

        Movie expectedParam = captor.getValue();
        assertEquals(id, expectedParam.getId());
        assertEquals("Disaster Artist", expectedParam.getTitle());
        assertEquals(testGenre, expectedParam.getGenre());
        assertEquals(100, expectedParam.getQty());
        assertEquals(new BigDecimal("1.00"), expectedParam.getDailyRate());

        assertEquals(expectedMovie, fromRepo);
    }

    @Test
    void findById() {
        given(toTest.findById(any(UUID.class))).willReturn(Optional.of(expectedMovie));

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Movie> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedMovie, fromRepo.get());
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
    void findByIdEmpty() {
        given(toTest.findById(any(UUID.class))).willReturn(Optional.empty());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Movie> fromRepo = toTest.findById(id);

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
        final List<Movie> expectedMovies = Arrays.asList(expectedMovie);

        given(toTest.findAll()).willReturn(expectedMovies);

        List<Movie> fromRepo = toTest.findAll();

        verify(toTest).findAll();

        assertEquals(expectedMovies, fromRepo);
    }

    @Test
    void findAllByGenre_Name() {
        final List<Movie> expectedMovies = Arrays.asList(expectedMovie);

        given(toTest.findAllByGenre_Id(any(UUID.class))).willReturn(expectedMovies);

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        List<Movie> fromRepo = toTest.findAllByGenre_Id(id);

        verify(toTest).findAllByGenre_Id(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);

        assertEquals(expectedMovies, fromRepo);
    }

}
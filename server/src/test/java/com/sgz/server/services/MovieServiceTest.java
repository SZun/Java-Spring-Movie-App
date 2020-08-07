package com.sgz.server.services;

import com.sgz.server.entities.Genre;
import com.sgz.server.entities.Movie;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.repos.MovieRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService toTest;

    @Mock
    private MovieRepo movieRepo;

    private final UUID id = new UUID(36, 36);

    private final Genre testGenre = new Genre(this.id, "Horror");

    private final Movie expectedMovie = new Movie(this.id, "Disaster Artist", this.testGenre, 00100, new BigDecimal("09.99"));

    private final String testLongStr = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    private final String testShortStr = "1234";

    @Test
    void getAllMovies() throws NoItemsException {
        final Movie expectedMovie2 = new Movie(this.id, "Wolfman", this.testGenre, 00100, new BigDecimal("09.99"));
        final Movie expectedMovie3 = new Movie(this.id, "The Avengers", this.testGenre, 00100, new BigDecimal("09.99"));

        when(movieRepo.findAll()).thenReturn(Arrays.asList(expectedMovie, expectedMovie2, expectedMovie3));

        List<Movie> fromService = toTest.getAllMovies();

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expectedMovie));
        assertTrue(fromService.contains(expectedMovie2));
        assertTrue(fromService.contains(expectedMovie3));
    }

    @Test
    void getAllMoviesNoItems() {
        assertThrows(NoItemsException.class,  () -> toTest.getAllMovies());
    }

    @Test
    void getAllByGenreName() throws InvalidEntityException, NoItemsException {
        final Movie expectedMovie2 = new Movie(this.id, "Wolfman", this.testGenre, 00100, new BigDecimal("09.99"));
        final Movie expectedMovie3 = new Movie(this.id, "The Avengers", this.testGenre, 00100, new BigDecimal("09.99"));

        when(movieRepo.findAllByGenre_Name(anyString())).thenReturn(Arrays.asList(expectedMovie, expectedMovie2, expectedMovie3));

        List<Movie> fromService = toTest.getAllByGenreName(testGenre.getName());

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expectedMovie));
        assertTrue(fromService.contains(expectedMovie2));
        assertTrue(fromService.contains(expectedMovie3));
    }

    @Test
    void getAllByGenreNameNullName(){
        assertThrows(InvalidEntityException.class, () -> toTest.getAllByGenreName(null));
    }

    @Test
    void getAllByGenreNameBlankName(){
        assertThrows(InvalidEntityException.class, () -> toTest.getAllByGenreName("  "));
    }

    @Test
    void getAllByGenreNameEmptyName(){
        assertThrows(InvalidEntityException.class, () -> toTest.getAllByGenreName(""));
    }

    @Test
    void getAllByGenreNameTooLongName(){
        assertThrows(InvalidEntityException.class, () -> toTest.getAllByGenreName(testLongStr));
    }

    @Test
    void getAllByGenreNameTooShortName(){
        assertThrows(InvalidEntityException.class, () -> toTest.getAllByGenreName(testShortStr));
    }

    @Test
    void getAllByGenreNameNoItems(){
        assertThrows(NoItemsException.class,  () -> toTest.getAllByGenreName(testGenre.getName()));
    }

    @Test
    void getMovieById() throws InvalidIdException {
        when(movieRepo.findById(any(UUID.class))).thenReturn(Optional.of(expectedMovie));

        Movie fromService = toTest.getMovieById(id);

        assertEquals(expectedMovie, fromService);
    }

    @Test
    void getMovieByIdInvalidId(){
        assertThrows(InvalidIdException.class, () -> toTest.getMovieById(id));
    }

    @Test
    void createMovie() throws InvalidEntityException {
        when(movieRepo.save(any(Movie.class))).thenReturn(expectedMovie);

        Movie fromService = toTest.createMovie(expectedMovie);

        assertEquals(expectedMovie, fromService);
    }

    @Test
    void createMovieNullMovie(){
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(null));
    }

    @Test
    void createMovieNullGenre(){
        final Movie toCreate = new Movie(this.id, "Disaster Artist", null, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void createMovieEmptyTitle(){
        final Movie toCreate = new Movie(this.id, "", this.testGenre, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void createMovieBlankTitle(){
        final Movie toCreate = new Movie(this.id, "  ", this.testGenre, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void createMovieTooLongTitle(){
        final Movie toCreate = new Movie(this.id, testLongStr, this.testGenre, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void createMovieTooShortTitle(){
        final Movie toCreate = new Movie(this.id, testShortStr, this.testGenre, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void createMovieTooLargeQty(){
        final Movie toCreate = new Movie(this.id, "Disaster Artist", this.testGenre, 100000, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void createMovieNegativeQty(){
        final Movie toCreate = new Movie(this.id, "Disaster Artist", this.testGenre, -1, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void createMovieTooLargeDailyRate(){
        final Movie toCreate = new Movie(this.id, "Disaster Artist", this.testGenre, 00100, new BigDecimal(100));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void createMovieNegativeDailyRate(){
        final Movie toCreate = new Movie(this.id, "Disaster Artist", this.testGenre, 00100, new BigDecimal(-100));
        assertThrows(InvalidEntityException.class, () -> toTest.createMovie(toCreate));
    }

    @Test
    void updateMovie() throws InvalidEntityException, InvalidIdException {
        when(movieRepo.existsById(any(UUID.class))).thenReturn(true);
        when(movieRepo.save(any(Movie.class))).thenReturn(expectedMovie);

        Movie fromService = toTest.updateMovie(expectedMovie);

        assertEquals(expectedMovie, fromService);
    }

    @Test
    void updateMovieNullMovie(){
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(null));
    }

    @Test
    void updateMovieNullGenre(){
        final Movie toEdit = new Movie(this.id, "The Avengers", null, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieEmptyTitle(){
        final Movie toEdit = new Movie(this.id, "  ", this.testGenre, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieBlankTitle(){
        final Movie toEdit = new Movie(this.id, "  ", this.testGenre, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieTooLongTitle(){
        final Movie toEdit = new Movie(this.id, testLongStr, this.testGenre, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieTooShortTitle(){
        final Movie toEdit = new Movie(this.id, testShortStr, this.testGenre, 00100, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieTooLargeQty(){
        final Movie toEdit = new Movie(this.id, "Disaster Artist", this.testGenre, 100000, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieNegativeQty(){
        final Movie toEdit = new Movie(this.id, "Disaster Artist", this.testGenre, -1, new BigDecimal("09.99"));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieTooLargeDailyRate(){
        final Movie toEdit = new Movie(this.id, "Disaster Artist", this.testGenre, 00100, new BigDecimal(100));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieNegativeDailyRate(){
        final Movie toEdit = new Movie(this.id, "Disaster Artist", this.testGenre, 00100, new BigDecimal(-100));
        assertThrows(InvalidEntityException.class, () -> toTest.updateMovie(toEdit));
    }

    @Test
    void updateMovieInvalidId(){
        assertThrows(InvalidIdException.class, () -> toTest.updateMovie(expectedMovie));
    }

    @Test
    void deleteMovieById() throws InvalidIdException {
        when(movieRepo.existsById(any(UUID.class))).thenReturn(true);
        toTest.deleteMovieById(id);
    }

    @Test
    void deleteMovieByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.deleteMovieById(id));
    }
}
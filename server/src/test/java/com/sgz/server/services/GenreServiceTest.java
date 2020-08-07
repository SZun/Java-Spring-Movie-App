package com.sgz.server.services;

import com.sgz.server.entities.Genre;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.InvalidNameException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.repos.GenreRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @InjectMocks
    private GenreService toTest;

    @Mock
    private GenreRepo genreRepo;

    private final UUID id = new UUID(36, 36);

    private final Genre expectedGenre = new Genre(this.id, "Horror");

    private final String testLongStr = "1ZvBWFVdBu62e6yT87rdELXaLP6KfY2wJ9ZRpw9KmZqzNFICvlNKgkCU28aKRpQb2I85EqAxr6Xb4A1Ct4yNEjTOAXgNyyIBEyTnjOYyN4piLPot1OYtnNftyVXZg6DSxlAGgYzBa5ATYzkSHo2EmIpNyc0NCXvFtPdwP1N30s1Fn63sBaQGdX8sZffYO29yTVtg4LLYRdrrP8aPmL2Pm3c3XySoA7KLLNIi8417yXnjzgdDQErkKiAuoR5REsdL";

    private final String testShortStr = "1234";

    @Test
    void getAllGenres() throws NoItemsException {
        final Genre expectedGenre2 = new Genre(this.id, "Action");
        final Genre expectedGenre3 = new Genre(this.id, "Adventure");

        when(genreRepo.findAll()).thenReturn(Arrays.asList(expectedGenre,expectedGenre2,expectedGenre3));

        List<Genre> fromService = toTest.getAllGenres();

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expectedGenre));
        assertTrue(fromService.contains(expectedGenre2));
        assertTrue(fromService.contains(expectedGenre3));
    }

    @Test
    void getAllGenresNoItems() {
        assertThrows(NoItemsException.class, () -> toTest.getAllGenres());
    }

    @Test
    void getGenreById() throws InvalidIdException {
        when(genreRepo.findById(any(UUID.class))).thenReturn(Optional.of(expectedGenre));

        Genre fromService = toTest.getGenreById(UUID.randomUUID());

        assertEquals(expectedGenre, fromService);
    }

    @Test
    void getGenreByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.getGenreById(UUID.randomUUID()));
    }

    @Test
    void createGenre() throws InvalidEntityException, InvalidNameException {
        when(genreRepo.save(any(Genre.class))).thenReturn(expectedGenre);

        Genre fromService = toTest.createGenre(expectedGenre);

        assertEquals(expectedGenre, fromService);
    }

    @Test
    void createGenreInvalidName() {
        final Genre toCreate = new Genre(this.id, "Horror");

        when(genreRepo.existsByName(anyString())).thenReturn(true);

        assertThrows(InvalidNameException.class, () -> toTest.createGenre(toCreate));
    }

    @Test
    void createGenreNullGenre() {
        assertThrows(InvalidEntityException.class, () -> toTest.createGenre(null));
    }

    @Test
    void createGenreBlankName() {
        final Genre toCreate = new Genre(this.id, " ");

        assertThrows(InvalidEntityException.class, () -> toTest.createGenre(toCreate));
    }

    @Test
    void createGenreEmptyName() {
        final Genre toCreate = new Genre(this.id, "");

        assertThrows(InvalidEntityException.class, () -> toTest.createGenre(toCreate));
    }

    @Test
    void createGenreTooShortName() {
        final Genre toCreate = new Genre(this.id, testShortStr);

        assertThrows(InvalidEntityException.class, () -> toTest.createGenre(toCreate));
    }

    @Test
    void createGenreTooLongName() {
        final Genre toCreate = new Genre(this.id, testLongStr);

        assertThrows(InvalidEntityException.class, () -> toTest.createGenre(toCreate));
    }

    @Test
    void editGenre() throws InvalidEntityException, InvalidIdException {
        when(genreRepo.save(any(Genre.class))).thenReturn(expectedGenre);
        when(genreRepo.existsById(any(UUID.class))).thenReturn(true);

        Genre fromService = toTest.editGenre(expectedGenre);

        assertEquals(expectedGenre, fromService);
    }

    @Test
    void editGenreInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.editGenre(expectedGenre));
    }

    @Test
    void editGenreNullGenre() {
        assertThrows(InvalidEntityException.class, () -> toTest.editGenre(null));
    }

    @Test
    void editGenreBlankName() {
        final Genre toEdit = new Genre(this.id, "  ");

        assertThrows(InvalidEntityException.class, () -> toTest.editGenre(toEdit));
    }

    @Test
    void editGenreEmptyName() {
        final Genre toEdit = new Genre(this.id, "");

        assertThrows(InvalidEntityException.class, () -> toTest.editGenre(toEdit));
    }

    @Test
    void editGenreTooShortName() {
        final Genre toEdit = new Genre(this.id, testShortStr);

        assertThrows(InvalidEntityException.class, () -> toTest.editGenre(toEdit));
    }

    @Test
    void editGenreTooLongName() {
        final Genre toEdit = new Genre(this.id, testLongStr);

        assertThrows(InvalidEntityException.class, () -> toTest.editGenre(toEdit));
    }

    @Test
    void deleteGenreById() throws InvalidIdException {
        when(genreRepo.existsById(any(UUID.class))).thenReturn(true);

        toTest.deleteGenreById(id);
    }

    @Test
    void deleteGenreByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.getGenreById(UUID.randomUUID()));
    }

    @Test
    void getGenreByName() throws InvalidEntityException, InvalidNameException {
        when(genreRepo.findByName(anyString())).thenReturn(Optional.of(expectedGenre));

        Genre fromService = toTest.getGenreByName("Horror");

        assertEquals(expectedGenre, fromService);
    }

    @Test
    void getGenreByNameInvalidName() {
        assertThrows(InvalidNameException.class, () -> toTest.getGenreByName("Horror"));
    }

    @Test
    void getGenreByNameNullName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getGenreByName(null));
    }

    @Test
    void getGenreByNameEmptyName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getGenreByName(""));
    }

    @Test
    void getGenreByNameBlankName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getGenreByName("  "));
    }

    @Test
    void getGenreByNameTooShortName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getGenreByName(testShortStr));
    }

    @Test
    void getGenreByNameTooLongName() {
        assertThrows(InvalidEntityException.class, () -> toTest.getGenreByName(testLongStr));
    }
}
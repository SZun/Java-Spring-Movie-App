package com.sgz.server.controllers;

import com.sgz.server.entities.Genre;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.InvalidNameException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() throws NoItemsException {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable UUID id) throws InvalidIdException {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre toAdd) throws InvalidEntityException, InvalidNameException {
        return new ResponseEntity(genreService.createGenre(toAdd), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Genre> updateGenre(@PathVariable UUID id, @Valid @RequestBody Genre toEdit) throws InvalidIdException, InvalidEntityException {
        try {
            Genre toCheck = genreService.getGenreByName(toEdit.getName());

            if (!toCheck.getId().equals(id)) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch(InvalidNameException | InvalidEntityException ex){}

        toEdit.setId(id);

        return ResponseEntity.ok(genreService.editGenre(toEdit));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<UUID> deleteGenreById(@PathVariable UUID id) throws InvalidIdException {
        genreService.deleteGenreById(id);
        return ResponseEntity.ok(id);
    }

}

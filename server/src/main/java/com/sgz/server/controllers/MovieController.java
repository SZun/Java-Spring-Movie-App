package com.sgz.server.controllers;

import com.sgz.server.entities.Genre;
import com.sgz.server.entities.Movie;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.models.MovieVM;
import com.sgz.server.services.GenreService;
import com.sgz.server.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() throws NoItemsException {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<List<Movie>> getAllMoviesByGenreId(@PathVariable UUID id) throws NoItemsException {
        return ResponseEntity.ok(movieService.getAllMoviesByGenreId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable UUID id) throws InvalidIdException {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody MovieVM toConvert) throws InvalidEntityException, InvalidIdException {
        Genre movieGenre = genreService.getGenreById(toConvert.getGenreId());
        Movie toAdd = new Movie(toConvert, movieGenre);

        return new ResponseEntity(movieService.createMovie(toAdd), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Movie> updateMovie(@PathVariable UUID id, @RequestBody MovieVM toConvert) throws InvalidIdException, InvalidEntityException {
        if(!toConvert.getGenreId().equals(id)) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        Genre movieGenre = genreService.getGenreById(toConvert.getGenreId());
        Movie toEdit = new Movie(toConvert, movieGenre);

        return ResponseEntity.ok(movieService.updateMovie(toEdit));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<UUID> deleteMovieById(@PathVariable UUID id) throws InvalidIdException {
        movieService.deleteMovieById(id);
        return ResponseEntity.ok(id);
    }

}

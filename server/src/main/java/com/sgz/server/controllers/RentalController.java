package com.sgz.server.controllers;

import com.sgz.server.entities.Rental;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.services.AuthService;
import com.sgz.server.services.MovieService;
import com.sgz.server.services.RentalService;
import com.sgz.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() throws NoItemsException {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public ResponseEntity<List<Rental>> getAllRentalsByUserIdEmployee(@PathVariable UUID id) throws NoItemsException {
        return ResponseEntity.ok(rentalService.getAllRentalsByUserId(id));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Rental>> getAllRentalsByUserId() throws NoItemsException {
        UUID userId = authService.getUserId();
        return ResponseEntity.ok(rentalService.getAllRentalsByUserId(authService.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable UUID id) throws InvalidIdException, AccessDeniedException {
        return ResponseEntity.ok(rentalService.getRentalById(id, authService.getUserId()));
    }

    @PostMapping("/{movieId}")
    public ResponseEntity<Rental> createRental(@PathVariable UUID movieId) throws InvalidEntityException, InvalidIdException {

        Rental toAdd = new Rental();
        toAdd.setMovie(movieService.getMovieById(movieId));
        toAdd.setUser(userService.getUserById(authService.getUserId()));

        return new ResponseEntity(rentalService.createRental(toAdd), HttpStatus.CREATED);
    }

}

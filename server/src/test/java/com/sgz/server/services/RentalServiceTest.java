package com.sgz.server.services;

import com.google.common.collect.Sets;
import com.sgz.server.entities.*;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.repos.RentalRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @InjectMocks
    private RentalService toTest;

    @Mock
    private RentalRepo rentalRepo;

    private final UUID id = new UUID(36, 36);

    private final Genre testGenre = new Genre(this.id, "Horror");

    private final Movie testMovie = new Movie(this.id, "Distarter Artist", this.testGenre, 100, new BigDecimal("1.00"));

    private final Set<Role> testRoles = Sets.newHashSet(Sets.newHashSet(new Role(this.id, "CUSTOMER")));

    private final User testUser = new User(this.id, "@amBam20", "Sam", this.testRoles);

    private final Rental expectedRental = new Rental(this.id, this.testMovie, this.testUser, LocalDate.of(2020, 8, 04), LocalDate.of(2020, 8, 11), null);

    @Test
    void getAllRentals() throws NoItemsException {
        final Rental expectedRental2 = new Rental(this.id, this.testMovie, testUser, LocalDate.of(2020, 8, 10), LocalDate.of(2020, 8, 17), null);
        final Rental expectedRental3 = new Rental(this.id, this.testMovie, testUser, LocalDate.of(2020, 8, 20), LocalDate.of(2020, 8, 27), null);

        when(rentalRepo.findAll()).thenReturn(Arrays.asList(expectedRental, expectedRental2, expectedRental3));

        List<Rental> fromService = toTest.getAllRentals();

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expectedRental));
        assertTrue(fromService.contains(expectedRental2));
        assertTrue(fromService.contains(expectedRental3));
    }

    @Test
    void getAllRentalsNoItems() {
        assertThrows(NoItemsException.class, () -> toTest.getAllRentals());
    }

    @Test
    void getAllRentalsByCustomerId() throws NoItemsException, AccessDeniedException {
        final Rental expectedRental2 = new Rental(id, testMovie, testUser, LocalDate.of(2020, 8, 10), LocalDate.of(2020, 8, 17), null);
        final Rental expectedRental3 = new Rental(id, testMovie, testUser, LocalDate.of(2020, 8, 20), LocalDate.of(2020, 8, 27), null);

        when(rentalRepo.findAllByUser_Id(any(UUID.class))).thenReturn(Arrays.asList(expectedRental, expectedRental2, expectedRental3));

        List<Rental> fromService = toTest.getAllRentalsByCustomerId(id, id);

        assertEquals(3, fromService.size());
        assertTrue(fromService.contains(expectedRental));
        assertTrue(fromService.contains(expectedRental2));
        assertTrue(fromService.contains(expectedRental3));
    }

    @Test
    void getAllRentalsByCustomerIdNoItems() {
        assertThrows(NoItemsException.class, () -> toTest.getAllRentalsByCustomerId(id, id));
    }

    @Test
    void getAllRentalsByCustomerIdAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> toTest.getAllRentalsByCustomerId(id, UUID.randomUUID()));
    }

    @Test
    void createRental() throws InvalidEntityException {
        when(rentalRepo.save(any(Rental.class))).thenReturn(expectedRental);

        Rental fromService = toTest.createRental(expectedRental);

        assertEquals(expectedRental, fromService);
    }

    @Test
    void createRentalNullRental() {
        assertThrows(InvalidEntityException.class, () -> toTest.createRental(null));
    }

    @Test
    void createRentalNullMovie() {
        final Rental expectedRental = new Rental(id, null, testUser, LocalDate.of(2020, 8, 04), LocalDate.of(2020, 8, 11), null);
        assertThrows(InvalidEntityException.class, () -> toTest.createRental(null));
    }

    @Test
    void createRentalNullRentalCustomer() {
        final Rental expectedRental = new Rental(id, testMovie, null, LocalDate.of(2020, 8, 04), LocalDate.of(2020, 8, 11), null);
        assertThrows(InvalidEntityException.class, () -> toTest.createRental(null));
    }

    @Test
    void getRentalById() throws InvalidIdException, AccessDeniedException {
        when(rentalRepo.findById(any(UUID.class))).thenReturn(Optional.of(expectedRental));

        Rental fromService = toTest.getRentalById(id, id);

        assertEquals(expectedRental, fromService);
    }

    @Test
    void getRentalByIdInvalidId() {
        assertThrows(InvalidIdException.class, () -> toTest.getRentalById(id, id));
    }

    @Test
    void getRentalByIdInvalidAccessDenied() {
        when(rentalRepo.findById(any(UUID.class))).thenReturn(Optional.of(expectedRental));
        assertThrows(AccessDeniedException.class, () -> toTest.getRentalById(id, UUID.randomUUID()));
    }
}
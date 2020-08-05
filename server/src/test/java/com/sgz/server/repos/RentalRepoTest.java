package com.sgz.server.repos;

import com.sgz.server.entities.Customer;
import com.sgz.server.entities.Genre;
import com.sgz.server.entities.Movie;
import com.sgz.server.entities.Rental;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
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
class RentalRepoTest {

    @Mock
    private RentalRepo toTest;

    private final UUID id = new UUID(36, 36);

    private final Genre testGenre = new Genre(this.id, "Horror");

    private final Movie testMovie = new Movie(this.id, "Distarter Artist", this.testGenre, 100, new BigDecimal("1.00"));

    private final Customer testCustomer = new Customer(this.id, "Sam", true, "1234567890");

    private final Rental expectedRental = new Rental(this.id, this.testMovie, this.testCustomer, LocalDate.of(2020, 8, 04), LocalDate.of(2020, 8, 11), null);

    @Test
    void save() {
        given(toTest.save(any(Rental.class))).willReturn(expectedRental);

        ArgumentCaptor<Rental> captor = ArgumentCaptor.forClass(Rental.class);

        Rental fromRepo = toTest.save(expectedRental);

        verify(toTest).save(captor.capture());

        Rental expectedParam = captor.getValue();
        assertEquals(id, expectedParam.getId());
        assertEquals(testMovie, expectedParam.getMovie());
        assertEquals(testGenre, expectedParam.getMovie().getGenre());
        assertEquals(testCustomer, expectedParam.getCustomer());
        assertEquals(LocalDate.of(2020, 8, 04), expectedParam.getRentalDate());
        assertEquals(LocalDate.of(2020, 8, 11), expectedParam.getReturnDate());
        assertEquals(new BigDecimal("7.00"), expectedParam.getFee());

        assertEquals(expectedRental, fromRepo);
    }

    @Test
    void findById() {
        given(toTest.findById(any(UUID.class))).willReturn(Optional.of(expectedRental));

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Optional<Rental> fromRepo = toTest.findById(id);

        verify(toTest).findById(captor.capture());

        UUID expectedParam = captor.getValue();

        assertEquals(id, expectedParam);
        assertTrue(fromRepo.isPresent());
        assertEquals(expectedRental, fromRepo.get());
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

        Optional<Rental> fromRepo = toTest.findById(id);

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
        final List<Rental> expectedRentals = Arrays.asList(expectedRental);

        given(toTest.findAll()).willReturn(expectedRentals);

        List<Rental> fromRepo = toTest.findAll();

        verify(toTest).findAll();

        assertEquals(expectedRentals, fromRepo);
    }

    @Test
    void findAllByGenre_Name() {
        final List<Rental> expectedRentals = Arrays.asList(expectedRental);

        given(toTest.findAllByCustomer_Name(anyString())).willReturn(expectedRentals);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        List<Rental> fromRepo = toTest.findAllByCustomer_Name("Sam");

        verify(toTest).findAllByCustomer_Name(captor.capture());

        String expectedParam = captor.getValue();

        assertEquals(testCustomer.getName(), expectedParam);

        assertEquals(expectedRentals, fromRepo);
    }

}
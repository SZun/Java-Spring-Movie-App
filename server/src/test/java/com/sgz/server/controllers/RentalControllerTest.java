package com.sgz.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgz.server.entities.Genre;
import com.sgz.server.entities.Movie;
import com.sgz.server.jwt.JwtConfig;
import com.sgz.server.jwt.JwtSecretKey;
import com.sgz.server.models.MovieVM;
import com.sgz.server.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RentalController.class)
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;

    @MockBean
    private MovieService movieService;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private JwtSecretKey jwtSecretKey;

    @MockBean
    private SecretKey secretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseURL = "/api/v1/movies";

    private String testUUIDStr = "00000000-0000-0024-0000-000000000024";

    private final String baseURLWithId = this.baseURL + "/" + this.testUUIDStr;

    private final UUID id = new UUID(36, 36);

    private final Genre testGenre = new Genre(this.id, "Horror");

    private final Movie testMovie = new Movie(this.id, "Disaster Artist", this.testGenre, 00100, new BigDecimal("09.99"));

    private final MovieVM testMovieVM = new MovieVM(this.id, "Disaster Artist", this.testGenre.getId(), 0100, new BigDecimal("09.99"));

    @Test
    void getAllRentals() throws Exception {
    }

    @Test
    void getAllRentalsNoItems() throws Exception {
    }

    @Test
    void getAllRentalsForbidden() throws Exception {
    }

    @Test
    void getAllRentalsByUserIdEmployee() throws Exception {
    }

    @Test
    void getAllRentalsByUserIdEmployeeNoItems() throws Exception {
    }

    @Test
    void getAllRentalsByUserIdEmployeeForbidden() throws Exception {
    }

    @Test
    void getAllRentalsByUserId() throws Exception {
    }

    @Test
    void getAllRentalsByUserIdNoItems() throws Exception {
    }

    @Test
    void getAllRentalsByUserIdAccessDenied() throws Exception {
    }

    @Test
    void getAllRentalsByUserIdForbidden() throws Exception {
    }

    @Test
    void getRentalById() throws Exception {
    }

    @Test
    void getRentalByIdInvalidId() throws Exception {
    }

    @Test
    void getRentalByIdAccessDenied() throws Exception {
    }

    @Test
    void getRentalByIdForbidden() throws Exception {
    }

    @Test
    void createRental() throws Exception {
    }

    @Test
    void createRentalInvalidIdException() throws Exception {
    }

    @Test
    void createRentalInvalidIdEntity() throws Exception {
    }

    @Test
    void createRentalInvalidForbiddent() throws Exception {
    }
}
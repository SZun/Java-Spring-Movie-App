package com.sgz.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgz.server.entities.Genre;
import com.sgz.server.jwt.JwtConfig;
import com.sgz.server.jwt.JwtSecretKey;
import com.sgz.server.services.GenreService;
import com.sgz.server.services.MovieService;
import com.sgz.server.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private GenreService genreService;

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

    private final String baseURL = "/api/v1/genres";

    private String testUUIDStr = "00000000-0000-0024-0000-000000000024";

    private final String baseURLWithId = this.baseURL + "/" + this.testUUIDStr;

    private final UUID id = new UUID(36, 36);

    private final Genre testGenre = new Genre(this.id, "Horror");

    @Test
    void getAllMovies() {
    }

    @Test
    void getAllMoviesByGenreId() {
    }

    @Test
    void getMovieById() {
    }

    @Test
    void createMovie() {
    }

    @Test
    void updateMovie() {
    }

    @Test
    void deleteMovieById() {
    }
}
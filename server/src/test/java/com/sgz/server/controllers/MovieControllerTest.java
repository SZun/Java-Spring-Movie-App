package com.sgz.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgz.server.entities.Genre;
import com.sgz.server.entities.Movie;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.jwt.JwtConfig;
import com.sgz.server.jwt.JwtSecretKey;
import com.sgz.server.models.MovieVM;
import com.sgz.server.services.GenreService;
import com.sgz.server.services.MovieService;
import com.sgz.server.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private final String baseURL = "/api/v1/movies";

    private String testUUIDStr = "00000000-0000-0024-0000-000000000024";

    private final String baseURLWithId = this.baseURL + "/" + this.testUUIDStr;

    private final UUID id = new UUID(36, 36);

    private final Genre testGenre = new Genre(this.id, "Horror");

    private final Movie testMovie = new Movie(this.id, "Disaster Artist", this.testGenre, 00100, new BigDecimal("09.99"));

    private final MovieVM testMovieVM = new MovieVM(this.id, "Disaster Artist", this.testGenre.getId(), 0100, new BigDecimal("09.99"));

    private final String expectedMovie = "{\"id\":\"00000000-0000-0024-0000-000000000024\",\"title\":\"Disaster Artist\",\"genre\":{\"id\":\"00000000-0000-0024-0000-000000000024\",\"name\":\"Horror\"},\"qty\":64,\"dailyRate\":9.99}";

    @Test
    @WithMockUser
    void getAllMovies() throws Exception {
        final String expected = "[" + expectedMovie + "]";

        when(movieService.getAllMovies()).thenReturn(Arrays.asList(testMovie));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void getAllMoviesNoItems() throws Exception {
        final String expectedMsg = "\"message\":\"No Items\",";
        final String expectedName = "\"name\":\"NoItemsException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(movieService.getAllMovies()).thenThrow(new NoItemsException("No Items"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getAllMoviesForbidden() throws Exception {
        mockMvc.perform(
                get(baseURL)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getAllMoviesByGenreName() throws Exception {
        final String expected = "[" + expectedMovie + "]";

        when(movieService.getAllMoviesByGenreId(any(UUID.class))).thenReturn(Arrays.asList(testMovie));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/genres/" + testUUIDStr))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void getAllMoviesByGenreNameNoItems() throws Exception {
        final String expectedMsg = "\"message\":\"No Items\",";
        final String expectedName = "\"name\":\"NoItemsException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(movieService.getAllMoviesByGenreId(any(UUID.class))).thenThrow(new NoItemsException("No Items"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/genres/" + testUUIDStr))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getAllMoviesByGenreNameForbidden() throws Exception {
        mockMvc.perform(
                get(baseURL + "/Horror")
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getMovieById() throws Exception {
        when(movieService.getMovieById(any(UUID.class))).thenReturn(testMovie);

        MvcResult mvcResult = mockMvc.perform(get(baseURLWithId))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedMovie, content);
    }

    @Test
    @WithMockUser
    void getMovieByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(movieService.getMovieById(any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(get(baseURLWithId))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getMovieByIdForbidden() throws Exception {
        mockMvc.perform(
                get(baseURLWithId)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void createMovie() throws Exception {
        when(genreService.getGenreById(any(UUID.class))).thenReturn(testGenre);
        when(movieService.createMovie(any(Movie.class))).thenReturn(testMovie);

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testMovieVM))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedMovie, content);
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void createMovieInvalidGenre() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.getGenreById(any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testMovieVM))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void createMovieInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.getGenreById(any(UUID.class))).thenReturn(testGenre);
        when(movieService.createMovie(any(Movie.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testMovieVM))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void createMovieForbidden() throws Exception {
        mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testMovie))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void updateMovie() throws Exception {
        when(genreService.getGenreById(any(UUID.class))).thenReturn(testGenre);
        when(movieService.updateMovie(any(Movie.class))).thenReturn(testMovie);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testMovieVM))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedMovie, content);
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void updateMovieInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.getGenreById(any(UUID.class))).thenReturn(testGenre);
        when(movieService.updateMovie(any(Movie.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testMovieVM))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void updateMovieInvalidGenre() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.getGenreById(any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testMovieVM))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void updateMovieInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(movieService.updateMovie(any(Movie.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testMovieVM))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void updateMovieForbidden() throws Exception {
        mockMvc.perform(
                put(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteMovieById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(baseURLWithId))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("\"" + testUUIDStr + "\"", content);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteMovieByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(mockMvc.perform(delete(baseURLWithId))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(delete(baseURLWithId))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void deleteMovieByIdForbidden() throws Exception {
        mockMvc.perform(
                delete(baseURLWithId)
        )
                .andExpect(status().isForbidden());
    }
}
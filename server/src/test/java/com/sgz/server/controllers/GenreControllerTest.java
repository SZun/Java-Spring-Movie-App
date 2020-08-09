package com.sgz.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgz.server.entities.Genre;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.InvalidNameException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.jwt.JwtConfig;
import com.sgz.server.jwt.JwtSecretKey;
import com.sgz.server.services.GenreService;
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
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void getAllGenres() throws Exception {
        final String expected = "[{\"id\":\"00000000-0000-0024-0000-000000000024\",\"name\":\"Horror\"}]";

        when(genreService.getAllGenres()).thenReturn(Arrays.asList(testGenre));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void getAllGenresNoItems() throws Exception {
        final String expectedMsg = "\"message\":\"No Items\",";
        final String expectedName = "\"name\":\"NoItemsException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.getAllGenres()).thenThrow(new NoItemsException("No Items"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getAllGenresForbidden() throws Exception {
        mockMvc.perform(
                get(baseURL)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void getGenreById() throws Exception {
        final String expected = "{\"id\":\"00000000-0000-0024-0000-000000000024\",\"name\":\"Horror\"}";

        when(genreService.getGenreById(any(UUID.class))).thenReturn(testGenre);

        MvcResult mvcResult = mockMvc.perform(get(baseURLWithId))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void getGenreByIdInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.getGenreById(any(UUID.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(get(baseURLWithId))
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void getGenreByIdInvalidForbidden() throws Exception {
        mockMvc.perform(
                get(baseURLWithId)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void createGenre() throws Exception {
        final String expected = "{\"id\":\"00000000-0000-0024-0000-000000000024\",\"name\":\"Horror\"}";

        when(genreService.createGenre(any(Genre.class))).thenReturn(testGenre);

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void createGenreInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.createGenre(any(Genre.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void createGenreInvalidName() throws Exception {
        final String expectedMsg = "\"message\":\"Name entered is invalid\",";
        final String expectedName = "\"name\":\"InvalidNameException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.createGenre(any(Genre.class))).thenThrow(new InvalidNameException("Invalid Name"));

        MvcResult mvcResult = mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    void createGenreForbidden() throws Exception {
        mockMvc.perform(
                post(baseURL)
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void updateGenre() throws Exception {
        final String expected = "{\"id\":\"00000000-0000-0024-0000-000000000024\",\"name\":\"Horror\"}";

        when(genreService.editGenre(any(Genre.class))).thenReturn(testGenre);
        when(genreService.getGenreByName(anyString())).thenReturn(testGenre);

        MvcResult mvcResult = mockMvc.perform(
                put(baseURLWithId)
                    .content(objectMapper.writeValueAsString(testGenre))
                    .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void updateGenreInvalidId() throws Exception {
        final String expectedMsg = "\"message\":\"Invalid Id\",";
        final String expectedName = "\"name\":\"InvalidIdException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.getGenreByName(anyString())).thenReturn(testGenre);
        when(genreService.editGenre(any(Genre.class))).thenThrow(new InvalidIdException("Invalid Id"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void updateGenreInvalidEntity() throws Exception {
        final String expectedMsg = "\"message\":\"Fields entered are invalid\",";
        final String expectedName = "\"name\":\"InvalidEntityException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(genreService.getGenreByName(anyString())).thenReturn(testGenre);
        when(genreService.editGenre(any(Genre.class))).thenThrow(new InvalidEntityException("Invalid Entity"));

        MvcResult mvcResult = mockMvc.perform(
                put(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"EMPLOYEE"})
    void updateGenreInvalidName() throws Exception {
        when(genreService.getGenreByName(anyString())).thenReturn(testGenre);

        mockMvc.perform(
                put(baseURL + "/" + UUID.randomUUID().toString())
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGenreForbidden() throws Exception {
        mockMvc.perform(
                put(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testGenre))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"ADMIN"})
    void deleteGenreById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(baseURLWithId))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("\"" + testUUIDStr + "\"", content);
    }

    @Test
    @WithMockUser(username = "Sam", roles = {"ADMIN"})
    void deleteGenreByIdInvalidId() throws Exception {
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
    void deleteGenreByIdForbidden() throws Exception {
        mockMvc.perform(
                delete(baseURLWithId)
        )
                .andExpect(status().isForbidden());
    }
}
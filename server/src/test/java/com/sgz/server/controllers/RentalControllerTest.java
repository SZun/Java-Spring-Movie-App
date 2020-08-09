package com.sgz.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.sgz.server.entities.*;
import com.sgz.server.jwt.JwtConfig;
import com.sgz.server.jwt.JwtSecretKey;
import com.sgz.server.services.*;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private final String baseURL = "/api/v1/rentals";

    private String testUUIDStr = "00000000-0000-0024-0000-000000000024";

    private final String baseURLWithId = this.baseURL + "/" + this.testUUIDStr;

    private final UUID id = new UUID(36, 36);

    private final Genre testGenre = new Genre(this.id, "Horror");

    private final Movie testMovie = new Movie(this.id, "Disaster Artist", this.testGenre, 00100, new BigDecimal("09.99"));

    private final Set<Role> testRoles = Sets.newHashSet(Sets.newHashSet(new Role(this.id, "CUSTOMER")));

    private final User testUser = new User(this.id, "@amBam20", "Sam", this.testRoles);

    private final Rental testRental = new Rental(this.id, this.testMovie, this.testUser, LocalDate.of(2020, 8, 04), LocalDate.of(2020, 8, 11), null);

    private final String expectedRental = "{\"id\":\"00000000-0000-0024-0000-000000000024\",\"movie\":{\"id\":\"00000000-0000-0024-0000-000000000024\",\"title\":\"Disaster Artist\",\"genre\":{\"id\":\"00000000-0000-0024-0000-000000000024\",\"name\":\"Horror\"},\"qty\":64,\"dailyRate\":9.99},\"user\":{\"id\":\"00000000-0000-0024-0000-000000000024\",\"password\":\"@amBam20\",\"username\":\"Sam\",\"roles\":[{\"id\":\"00000000-0000-0024-0000-000000000024\",\"authority\":\"CUSTOMER\"}]},\"rentalDate\":\"2020-08-04\",\"returnDate\":\"2020-08-11\",\"fee\":69.93}";

    @Test
    @WithMockUser
    void getAllRentals() throws Exception {
        final String expected = "[" + expectedRental + "]";

        when(rentalService.getAllRentals()).thenReturn(Arrays.asList(testRental));

        MvcResult mvcResult = mockMvc.perform(get(baseURL))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void getAllRentalsNoItems() throws Exception {
    }

    @Test
    void getAllRentalsForbidden() throws Exception {
        mockMvc.perform(
                get(baseURL)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void getAllRentalsByUserIdEmployee() throws Exception {
        final String expected = "[" + expectedRental + "]";

        when(authService.getUserId()).thenReturn(id);
        when(rentalService.getAllRentalsByUserId(any(UUID.class))).thenReturn(Arrays.asList(testRental));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/users/" + testUUIDStr))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void getAllRentalsByUserIdEmployeeNoItems() throws Exception {
    }

    @Test
    void getAllRentalsByUserIdEmployeeForbidden() throws Exception {
        mockMvc.perform(
                get(baseURL + "/users/" + testUUIDStr)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getAllRentalsByUserId() throws Exception {
        final String expected = "[" + expectedRental + "]";

        when(authService.getUserId()).thenReturn(id);
        when(rentalService.getAllRentalsByUserId(any(UUID.class))).thenReturn(Arrays.asList(testRental));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/mine"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, content);
    }

    @Test
    @WithMockUser
    void getAllRentalsByUserIdNoItems() throws Exception {
    }

    @Test
    void getAllRentalsByUserIdForbidden() throws Exception {
        mockMvc.perform(
                get(baseURL + "/mine")
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void getRentalById() throws Exception {
        when(authService.getUserId()).thenReturn(id);
        when(rentalService.getRentalById(any(UUID.class), any(UUID.class))).thenReturn(testRental);

        MvcResult mvcResult = mockMvc.perform(get(baseURLWithId))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedRental, content);
    }

    @Test
    @WithMockUser
    void getRentalByIdInvalidId() throws Exception {
    }

    @Test
    @WithMockUser
    void getRentalByIdAccessDenied() throws Exception {
    }

    @Test
    void getRentalByIdForbidden() throws Exception {
        mockMvc.perform(
                get(baseURLWithId)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void createRental() throws Exception {
        when(movieService.getMovieById(any(UUID.class))).thenReturn(testMovie);
        when(userService.getUserById(any(UUID.class))).thenReturn(testUser);
        when(authService.getUserId()).thenReturn(id);
        when(rentalService.createRental(any(Rental.class))).thenReturn(testRental);

        MvcResult mvcResult = mockMvc.perform(
                post(baseURLWithId)
                        .content(objectMapper.writeValueAsString(id))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedRental, content);
    }

    @Test
    @WithMockUser
    void createRentalInvalidIdException() throws Exception {
    }

    @Test
    @WithMockUser
    void createRentalInvalidIdEntity() throws Exception {
    }

    @Test
    void createRentalInvalidForbidden() throws Exception {
        mockMvc.perform(
                post(baseURLWithId)
                        .content(objectMapper.writeValueAsString(testRental))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }
}
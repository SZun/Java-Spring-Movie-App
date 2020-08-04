package com.sgz.server.exceptionhandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgz.server.controllers.UserController;
import com.sgz.server.jwt.JwtConfig;
import com.sgz.server.jwt.JwtSecretKey;
import com.sgz.server.services.AuthService;
import com.sgz.server.services.RoleService;
import com.sgz.server.services.UserDetailsServiceImpl;
import com.sgz.server.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.crypto.SecretKey;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class ExceptionHandlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private AuthService authService;

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

    private final String baseURL = "/api/v1/users/";

    private final UUID id = new UUID(36, 36);

    private String testUUIDStr = "11a9c792-45c5-4220-ab7c-fb832b282911";

    @Test
    @WithMockUser
    void handleNumberFormatException() throws Exception {
        final String expectedMsg = "\"message\":\"Number Format Exception\",";
        final String expectedName = "\"name\":\"NumberFormatException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserById(any(UUID.class))).thenThrow(new NumberFormatException("Number format exception"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isBadRequest()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }

    @Test
    @WithMockUser
    void handleIllegalArgumentException() throws Exception {
        final String expectedMsg = "\"message\":\"Illegal argument\",";
        final String expectedName = "\"name\":\"IllegalArgumentException\",";
        final String expectedErrors = "\"errors\":null,\"timestamp\"";

        when(userService.getUserById(any(UUID.class))).thenThrow(new IllegalArgumentException("Illegal Argument"));

        MvcResult mvcResult = mockMvc.perform(get(baseURL + "/" + testUUIDStr))
                .andExpect(status().isBadRequest()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(expectedMsg));
        assertTrue(content.contains(expectedName));
        assertTrue(content.contains(expectedErrors));
    }
}
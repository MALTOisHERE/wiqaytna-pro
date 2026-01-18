package com.wiqaytna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiqaytna.dto.AuthResponse;
import com.wiqaytna.dto.LoginRequest;
import com.wiqaytna.dto.RegisterRequest;
import com.wiqaytna.dto.UserDTO;
import com.wiqaytna.exception.DuplicateResourceException;
import com.wiqaytna.model.UserRole;
import com.wiqaytna.security.JwtAuthenticationFilter;
import com.wiqaytna.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private UserDTO userDTO;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(UserRole.PATIENT);
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setPhone("+212612345678");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setRole(UserRole.PATIENT);

        authResponse = new AuthResponse("jwt-token", userDTO);
    }

    @Test
    void testRegister_Success() throws Exception {
        when(userService.register(any(RegisterRequest.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testRegister_DuplicateEmail_ReturnsBadRequest() throws Exception {
        when(userService.register(any(RegisterRequest.class)))
                .thenThrow(new DuplicateResourceException("User", "email", "test@example.com"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_Success() throws Exception {
        when(userService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void testLogin_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        when(userService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCurrentUser_Success() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(userDTO);

        mockMvc.perform(get("/api/auth/me")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetCurrentUser_NotFound_ReturnsNotFound() throws Exception {
        when(userService.getUserByEmail("nonexistent@example.com"))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/auth/me")
                        .param("email", "nonexistent@example.com"))
                .andExpect(status().isNotFound());
    }
}
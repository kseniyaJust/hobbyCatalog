package org.example.hobbycatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hobbycatalog.DTO.AuthRequestDTO;
import org.example.hobbycatalog.DTO.AuthResponseDTO;
import org.example.hobbycatalog.DTO.RegistUserDTO;
import org.example.hobbycatalog.service.SecurityTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersInfoController.class)
class UsersInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SecurityTokenService securityTokenService;

    private RegistUserDTO registUserDTO;
    private AuthRequestDTO authRequestDTO;
    private AuthResponseDTO authResponseDTO;

    @BeforeEach
    void setUp() {
        registUserDTO = new RegistUserDTO();
        registUserDTO.setEmail("test@example.com");
        registUserDTO.setPassword("password123");
        registUserDTO.setName("Test User");

        authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail("test@example.com");
        authRequestDTO.setPassword("password123");

        authResponseDTO = new AuthResponseDTO("access-token", "refresh-token");
    }

    @Test
    void registUser_Success() throws Exception {
        when(securityTokenService.registerUser(any(RegistUserDTO.class)))
                .thenReturn("You were registered. Log in your account");

        mockMvc.perform(post("/users/info/regist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registUserDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("You were registered. Log in your account"));
    }

    @Test
    void registUser_InvalidData_ReturnsBadRequest() throws Exception {
        registUserDTO.setEmail("invalid-email");

        mockMvc.perform(post("/users/info/regist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginUser_Success() throws Exception {
        when(securityTokenService.loginUser(any(AuthRequestDTO.class))).thenReturn(authResponseDTO);

        mockMvc.perform(post("/users/info/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void refreshToken_Success() throws Exception {
        Map<String, String> refreshTokenMap = new HashMap<>();
        refreshTokenMap.put("refreshToken", "refresh-token");

        when(securityTokenService.refreshUser(any(Map.class))).thenReturn(authResponseDTO);

        mockMvc.perform(post("/users/info/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"));
    }
}
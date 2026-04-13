package org.example.hobbycatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hobbycatalog.DTO.HobbyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createHobby_AsAdmin_ReturnsCreated() throws Exception {
        HobbyDTO hobbyDTO = new HobbyDTO();
        hobbyDTO.setName("Test Hobby");
        hobbyDTO.setPrice(100);
        hobbyDTO.setCreator("Test Creator");
        hobbyDTO.setTypeName("Board Game");

        mockMvc.perform(post("/hobbies/catalog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hobbyDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createHobby_AsUser_ReturnsForbidden() throws Exception {
        HobbyDTO hobbyDTO = new HobbyDTO();
        hobbyDTO.setName("Test Hobby");
        hobbyDTO.setPrice(100);
        hobbyDTO.setCreator("Test Creator");
        hobbyDTO.setTypeName("Board Game");

        mockMvc.perform(post("/hobbies/catalog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hobbyDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getHobbies_WithoutAuth_ReturnsOk() throws Exception {
        mockMvc.perform(get("/hobbies/catalog"))
                .andExpect(status().isOk());
    }
}
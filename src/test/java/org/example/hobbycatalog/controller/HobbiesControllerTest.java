package org.example.hobbycatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hobbycatalog.DTO.HobbyDTO;
import org.example.hobbycatalog.DTO.UpdateHobbyDTO;
import org.example.hobbycatalog.entity.Hobbies;
import org.example.hobbycatalog.entity.TypeHobbies;
import org.example.hobbycatalog.service.HobbiesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HobbiesController.class)
class HobbiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HobbiesService hobbiesService;

    private HobbyDTO hobbyDTO;
    private Hobbies hobby;
    private UpdateHobbyDTO updateHobbyDTO;

    @BeforeEach
    void setUp() {
        hobbyDTO = new HobbyDTO();
        hobbyDTO.setIdHobby(1L);
        hobbyDTO.setName("Test Hobby");
        hobbyDTO.setPrice(100);
        hobbyDTO.setCreator("Test Creator");
        hobbyDTO.setTypeName("Board Game");

        TypeHobbies type = new TypeHobbies();
        type.setIdType(1L);
        type.setTypeName("Board Game");

        hobby = new Hobbies();
        hobby.setIdHobby(1L);
        hobby.setName("Test Hobby");
        hobby.setPrice(100);
        hobby.setCreator("Test Creator");
        hobby.setTypeAndHobbies(type);

        updateHobbyDTO = new UpdateHobbyDTO();
        updateHobbyDTO.setName("Updated Hobby");
        updateHobbyDTO.setPrice(150);
    }

    @Test
    void getAllCatalog_WithoutAuth_ReturnsOk() throws Exception {
        List<HobbyDTO> hobbies = Arrays.asList(hobbyDTO);
        when(hobbiesService.getHobbyPagedResponse()).thenReturn(hobbies);

        mockMvc.perform(get("/hobbies/catalog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Hobby"));
    }

    @Test
    void getOneHobby_WithoutAuth_ReturnsOk() throws Exception {
        when(hobbiesService.getHobbyById(1L)).thenReturn(hobby);

        mockMvc.perform(get("/hobbies/catalog/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Hobby"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createHobby_AsAdmin_ReturnsCreated() throws Exception {
        when(hobbiesService.createNewHobby(any(HobbyDTO.class))).thenReturn(hobbyDTO);

        mockMvc.perform(post("/hobbies/catalog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hobbyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Hobby"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createHobby_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/hobbies/catalog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hobbyDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeHobby_AsAdmin_Success() throws Exception {
        when(hobbiesService.updateNewHobby(anyLong(), any(UpdateHobbyDTO.class))).thenReturn(hobbyDTO);

        mockMvc.perform(put("/hobbies/catalog/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateHobbyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Hobby"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteHobby_AsAdmin_Success() throws Exception {
        when(hobbiesService.deleteHobbyById(1L)).thenReturn("Hobby deleted");

        mockMvc.perform(delete("/hobbies/catalog/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hobby deleted"));
    }
}
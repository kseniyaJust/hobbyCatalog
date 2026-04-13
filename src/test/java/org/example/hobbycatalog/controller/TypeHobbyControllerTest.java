package org.example.hobbycatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hobbycatalog.DTO.TypeHobbiesDTO;
import org.example.hobbycatalog.DTO.UpdateTypeHobbyDTO;
import org.example.hobbycatalog.service.TypeHobbyService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TypeHobbyController.class)
class TypeHobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TypeHobbyService typeHobbyService;

    private TypeHobbiesDTO typeHobbiesDTO;
    private UpdateTypeHobbyDTO updateTypeHobbyDTO;

    @BeforeEach
    void setUp() {
        typeHobbiesDTO = new TypeHobbiesDTO();
        typeHobbiesDTO.setIdType(1L);
        typeHobbiesDTO.setTypeName("Board Game");
        typeHobbiesDTO.setCountPlayers(4);
        typeHobbiesDTO.setSummary("Fun board games");

        updateTypeHobbyDTO = new UpdateTypeHobbyDTO();
        updateTypeHobbyDTO.setTypeName("Card Game");
        updateTypeHobbyDTO.setCountPlayers(2);
    }

    @Test
    void getAllTypeHobbies_WithoutAuth_ReturnsOk() throws Exception {
        List<TypeHobbiesDTO> types = Arrays.asList(typeHobbiesDTO);
        when(typeHobbyService.getAllTypeHobbies()).thenReturn(types);

        mockMvc.perform(get("/hobbies/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].typeName").value("Board Game"));
    }

    @Test
    void getTypeHobbyById_WithoutAuth_ReturnsOk() throws Exception {
        when(typeHobbyService.getTypeHobbyById(1L)).thenReturn(typeHobbiesDTO);

        mockMvc.perform(get("/hobbies/types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeName").value("Board Game"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTypeHobby_AsAdmin_Success() throws Exception {
        when(typeHobbyService.createTypeHobby(any(TypeHobbiesDTO.class))).thenReturn(typeHobbiesDTO);

        mockMvc.perform(post("/hobbies/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeHobbiesDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeName").value("Board Game"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createTypeHobby_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/hobbies/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeHobbiesDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTypeHobby_AsAdmin_Success() throws Exception {
        when(typeHobbyService.updateTypeHobby(anyLong(), any(UpdateTypeHobbyDTO.class))).thenReturn(typeHobbiesDTO);

        mockMvc.perform(put("/hobbies/types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTypeHobbyDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTypeHobby_AsAdmin_Success() throws Exception {
        doNothing().when(typeHobbyService).deleteTypeHobby(1L);

        mockMvc.perform(delete("/hobbies/types/1"))
                .andExpect(status().isOk());
    }
}
package org.example.hobbycatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hobbycatalog.DTO.UpdateUserAddressDTO;
import org.example.hobbycatalog.DTO.UserAddressDTO;
import org.example.hobbycatalog.entity.UserAdress;
import org.example.hobbycatalog.service.UserAddressService;
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

@WebMvcTest(UserAdressController.class)
class UserAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserAddressService userAddressService;

    private UserAdress userAddress;
    private UserAddressDTO userAddressDTO;
    private UpdateUserAddressDTO updateUserAddressDTO;

    @BeforeEach
    void setUp() {
        userAddress = new UserAdress();
        userAddress.setId_adress(1L);
        userAddress.setCity("Moscow");
        userAddress.setStreet("Tverskaya");
        userAddress.setNumber_home(10);
        userAddress.setNumber_flat(5);

        userAddressDTO = new UserAddressDTO();
        userAddressDTO.setCity("Moscow");
        userAddressDTO.setStreet("Tverskaya");
        userAddressDTO.setNumber_home(10);
        userAddressDTO.setNumber_flat(5);

        updateUserAddressDTO = new UpdateUserAddressDTO();
        updateUserAddressDTO.setCity("Saint Petersburg");
        updateUserAddressDTO.setStreet("Nevsky");
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getAllAddresses_Success() throws Exception {
        List<UserAdress> addresses = Arrays.asList(userAddress);
        when(userAddressService.getAllUserAdresses()).thenReturn(addresses);

        mockMvc.perform(get("/users/adress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Moscow"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getOneAddress_Success() throws Exception {
        when(userAddressService.getOneAddress(1L)).thenReturn(userAddress);

        mockMvc.perform(get("/users/adress/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Moscow"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void addAddress_Success() throws Exception {
        when(userAddressService.createNewAddress(any(UserAddressDTO.class))).thenReturn(userAddressDTO);

        mockMvc.perform(post("/users/adress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddressDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Moscow"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void updateAddress_Success() throws Exception {
        when(userAddressService.updateAddress(anyLong(), any(UpdateUserAddressDTO.class))).thenReturn(userAddressDTO);

        mockMvc.perform(put("/users/adress/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserAddressDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void deleteAddress_Success() throws Exception {
        when(userAddressService.deleteAddress(1L)).thenReturn("Address deleted");

        mockMvc.perform(delete("/users/adress/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Address deleted"));
    }
}
package org.example.hobbycatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hobbycatalog.DTO.WalletDTO;
import org.example.hobbycatalog.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    private WalletDTO walletDTO;

    @BeforeEach
    void setUp() {
        walletDTO = new WalletDTO();
        walletDTO.setOwner_name("Test Owner");
        walletDTO.setCart_number(1234567890123456L);
        walletDTO.setCVC(123L);
        walletDTO.setDate_expire(new Date(System.currentTimeMillis()));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getAllWallets_Success() throws Exception {
        List<WalletDTO> wallets = Arrays.asList(walletDTO);
        when(walletService.getAllUserWallets()).thenReturn(wallets);

        mockMvc.perform(get("/users/wallet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].owner_name").value("Test Owner"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getWalletById_Success() throws Exception {
        when(walletService.getWalletById(1L)).thenReturn(walletDTO);

        mockMvc.perform(get("/users/wallet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner_name").value("Test Owner"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getBalance_Success() throws Exception {
        when(walletService.getTotalBalance()).thenReturn(1000);

        mockMvc.perform(get("/users/wallet/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void addWallet_Success() throws Exception {
        when(walletService.createWallet(any(WalletDTO.class))).thenReturn(walletDTO);

        mockMvc.perform(post("/users/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner_name").value("Test Owner"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void updateWallet_Success() throws Exception {
        when(walletService.updateWallet(anyLong(), any(WalletDTO.class))).thenReturn(walletDTO);

        mockMvc.perform(put("/users/wallet/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void deleteWallet_Success() throws Exception {
        doNothing().when(walletService).deleteWallet(1L);

        mockMvc.perform(delete("/users/wallet/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void addMoney_Success() throws Exception {
        when(walletService.addMoney(anyLong(), anyInt())).thenReturn("Successfully added 500");

        mockMvc.perform(put("/users/wallet/1/add-money")
                        .param("amount", "500"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully added 500"));
    }

    @Test
    void getAllWallets_WithoutAuth_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/users/wallet"))
                .andExpect(status().isUnauthorized());
    }
}
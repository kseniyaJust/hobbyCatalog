package org.example.hobbycatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hobbycatalog.DTO.CartDTO;
import org.example.hobbycatalog.DTO.PurchaseDTO;
import org.example.hobbycatalog.DTO.PurchaseResponseDTO;
import org.example.hobbycatalog.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    private CartDTO cartDTO;
    private PurchaseResponseDTO purchaseResponseDTO;

    @BeforeEach
    void setUp() {
        cartDTO = new CartDTO();
        cartDTO.setId_cart(1L);
        cartDTO.setAmount(500);
        cartDTO.setHobbies(new HashSet<>());

        purchaseResponseDTO = new PurchaseResponseDTO(
                "Purchase successful!",
                500,
                1000,
                Collections.emptyList()
        );
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getCart_Success() throws Exception {
        when(cartService.getCart()).thenReturn(cartDTO);

        mockMvc.perform(get("/users/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_cart").value(1L))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void addItemToCart_Success() throws Exception {
        when(cartService.addItemToCart(anyLong(), anyInt())).thenReturn(cartDTO);

        mockMvc.perform(post("/users/cart/1")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_cart").value(1L));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void updateItemQuantity_Success() throws Exception {
        when(cartService.updateItemQuantity(anyLong(), anyInt())).thenReturn(cartDTO);

        mockMvc.perform(put("/users/cart/1")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void deleteItemFromCart_Success() throws Exception {
        when(cartService.deleteItemFromCart(anyLong())).thenReturn(cartDTO);

        mockMvc.perform(delete("/users/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_cart").value(1L));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void clearCart_Success() throws Exception {
        when(cartService.clearCart()).thenReturn(cartDTO);

        mockMvc.perform(delete("/users/cart/clear"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void purchaseItems_Success() throws Exception {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setPurchaseAll(true);

        when(cartService.purchaseItems(any(PurchaseDTO.class))).thenReturn(purchaseResponseDTO);

        mockMvc.perform(post("/users/cart/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Purchase successful!"))
                .andExpect(jsonPath("$.totalAmount").value(500));
    }

    @Test
    void getCart_WithoutAuth_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/users/cart"))
                .andExpect(status().isUnauthorized());
    }
}
package org.example.hobbycatalog.controller;

import jakarta.validation.Valid;
import org.example.hobbycatalog.DTO.CartDTO;
import org.example.hobbycatalog.DTO.PurchaseDTO;
import org.example.hobbycatalog.DTO.PurchaseResponseDTO;
import org.example.hobbycatalog.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartDTO getCart() {
        return cartService.getCart();
    }

    @PostMapping("/{hobbyId}")
    public CartDTO addItemToCart(@PathVariable Long hobbyId,
                                 @RequestParam(defaultValue = "1") int quantity) {
        return cartService.addItemToCart(hobbyId, quantity);
    }

    @PutMapping("/{hobbyId}")
    public CartDTO updateItemQuantity(@PathVariable Long hobbyId,
                                      @RequestParam int quantity) {
        return cartService.updateItemQuantity(hobbyId, quantity);
    }

    @DeleteMapping("/{hobbyId}")
    public CartDTO deleteItemFromCart(@PathVariable Long hobbyId) {
        return cartService.deleteItemFromCart(hobbyId);
    }

    @DeleteMapping("/clear")
    public CartDTO clearCart() {
        return cartService.clearCart();
    }

    @PostMapping("/purchase")
    public PurchaseResponseDTO purchaseItems(@Valid @RequestBody PurchaseDTO purchaseDTO) {
        return cartService.purchaseItems(purchaseDTO);
    }
}
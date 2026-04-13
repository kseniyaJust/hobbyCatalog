package org.example.hobbycatalog.controller;

import jakarta.validation.Valid;
import org.example.hobbycatalog.DTO.WalletDTO;
import org.example.hobbycatalog.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/wallet")
@PreAuthorize("isAuthenticated()") // Все методы требуют аутентификации
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public List<WalletDTO> getAllWallets() {
        return walletService.getAllUserWallets();
    }

    @GetMapping("/{walletId}")
    public WalletDTO getWalletById(@PathVariable Long walletId) {
        return walletService.getWalletById(walletId);
    }

    @GetMapping("/balance")
    public int getBalance() {
        return walletService.getTotalBalance();
    }

    @PostMapping
    public WalletDTO addWallet(@Valid @RequestBody WalletDTO wallet) {
        return walletService.createWallet(wallet);
    }

    @PutMapping("/{walletId}")
    public WalletDTO updateWallet(@PathVariable Long walletId, @Valid @RequestBody WalletDTO wallet) {
        return walletService.updateWallet(walletId, wallet);
    }

    @DeleteMapping("/{walletId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWallet(@PathVariable Long walletId) {
        walletService.deleteWallet(walletId);
    }

    @PutMapping("/{walletId}/add-money")
    public String addMoney(@PathVariable Long walletId, @RequestParam int amount) {
        return walletService.addMoney(walletId, amount);
    }
}
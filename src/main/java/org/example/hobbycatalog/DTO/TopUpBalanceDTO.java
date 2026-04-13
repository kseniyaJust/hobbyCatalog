package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TopUpBalanceDTO {

    @NotNull(message = "Wallet ID is required")
    private Long walletId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least 1")
    private int amount;
}
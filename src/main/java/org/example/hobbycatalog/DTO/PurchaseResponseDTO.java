package org.example.hobbycatalog.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseResponseDTO {
    private String message;
    private int totalAmount;
    private int remainingBalance;
    private List<HobbyDTO> purchasedItems;
}

package org.example.hobbycatalog.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceDTO {
    private int currentBalance;
    private String message;
}
package org.example.hobbycatalog.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private String name;
    private int quantity;
    private double price;
    private double totalPrice;
}
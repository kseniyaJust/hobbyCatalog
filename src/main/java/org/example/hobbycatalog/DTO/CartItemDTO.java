package org.example.hobbycatalog.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private String name;      // Название товара
    private int quantity;     // Количество
    private double price;     // Цена за единицу
    private double totalPrice; // Общая цена за этот товар (price * quantity)
}
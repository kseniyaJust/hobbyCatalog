package org.example.hobbycatalog.DTO;

import lombok.Data;
import java.util.List;

@Data
public class CartDTO {
    private List<CartItemDTO> items;  // Список товаров в корзине
    private int totalAmount;          // Общая сумма корзины
}
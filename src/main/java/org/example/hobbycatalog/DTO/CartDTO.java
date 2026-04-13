package org.example.hobbycatalog.DTO;

import lombok.Data;
import java.util.List;

@Data
public class CartDTO {
    private List<CartItemDTO> items;
    private int totalAmount;
}
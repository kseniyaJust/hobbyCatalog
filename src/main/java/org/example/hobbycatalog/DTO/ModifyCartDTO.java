package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ModifyCartDTO {
    @NotNull(message = "Hobby ID is required")
    private Long hobbyId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private int quantity;
}
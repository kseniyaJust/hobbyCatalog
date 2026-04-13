package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HobbyDTO {

    private Long idHobby;

    @NotNull(message = "Type name is required")
    @NotEmpty(message = "Type name cannot be empty")
    @Size(min = 1, max = 100, message = "Type name must be between 1 and 100 characters")
    private String typeName;

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotNull(message = "Creator is required")
    @NotEmpty(message = "Creator cannot be empty")
    @Size(min = 1, max = 100, message = "Creator must be between 1 and 100 characters")
    private String creator;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private double price;
}
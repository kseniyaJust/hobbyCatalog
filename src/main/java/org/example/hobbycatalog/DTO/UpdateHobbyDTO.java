package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateHobbyDTO {

    @Size(min = 1, max = 100, message = "Type name must be between 1 and 100 characters")
    private String nameType;

    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Size(min = 1, max = 100, message = "Creator must be between 1 and 100 characters")
    private String creator;

    @Positive(message = "Price must be positive")
    private double price;
}
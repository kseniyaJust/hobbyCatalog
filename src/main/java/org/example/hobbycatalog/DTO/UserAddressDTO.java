package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UserAddressDTO {

    private Long id_adress;

    @NotNull(message = "City is required")
    @NotEmpty(message = "City cannot be empty")
    private String city;

    @NotNull(message = "Street is required")
    @NotEmpty(message = "Street cannot be empty")
    private String street;

    @NotNull(message = "Home number is required")
    @Positive(message = "Home number must be positive")
    private int number_home;

    @NotNull(message = "Flat number is required")
    @Positive(message = "Flat number must be positive")
    private int number_flat;
}
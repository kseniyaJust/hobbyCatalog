package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserAddressDTO {

    private Long id_adress;

    @NotEmpty
    private String city;

    @NotEmpty
    private String street;

    @NotEmpty
    private int number_home;

    @NotEmpty
    private int number_flat;
}

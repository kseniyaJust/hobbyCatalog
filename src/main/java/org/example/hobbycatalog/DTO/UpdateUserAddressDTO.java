package org.example.hobbycatalog.DTO;

import lombok.Data;

@Data
public class UpdateUserAddressDTO {
    private String city;

    private String street;

    private int number_home;

    private int number_flat;
}

package org.example.hobbycatalog.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.hobbycatalog.enumpackage.Role;

@Data
public class RegistUserDTO {
    private String name;

    private String email;

    private String password;

    private Role role;

    private int balance_amount;
}

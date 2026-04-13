package org.example.hobbycatalog.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.hobbycatalog.enumpackage.Role;

@Data
public class UserDTO {
    private String name;

    private String email;

    private String password;

    private Role role;
}

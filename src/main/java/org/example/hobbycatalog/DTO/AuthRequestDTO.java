package org.example.hobbycatalog.DTO;

import lombok.Data;
import org.example.hobbycatalog.enumpackage.Role;

@Data
public class AuthRequestDTO {
    private String email;

    private String password;

}

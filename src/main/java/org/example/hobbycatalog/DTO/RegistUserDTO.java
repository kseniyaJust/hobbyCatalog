package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.hobbycatalog.enumpackage.Role;

@Data
public class RegistUserDTO {

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    private Role role;

    @Min(value = 0, message = "Balance amount cannot be negative")
    private int balance_amount;
}

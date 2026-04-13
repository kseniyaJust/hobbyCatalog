package org.example.hobbycatalog.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.hobbycatalog.entity.TypeHobbies;

import java.util.Set;

@Data
public class HobbyDTO {

    private Long idHobby;

    @NotNull(message = "Type name is required")
    @NotEmpty(message = "Type name cannot be empty")
    private String typeName; 

    @NotEmpty
    @Size(min =1, max = 50)
    private String name;

    @NotEmpty
    @Size(min =1, max = 100)
    private String creator;

    @NotNull
    @Positive
    private double price;

}

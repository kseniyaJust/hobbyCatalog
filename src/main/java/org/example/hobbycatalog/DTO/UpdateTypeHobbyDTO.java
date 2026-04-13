package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTypeHobbyDTO {

    @Size(min = 1, max = 100, message = "Type name must be between 1 and 100 characters")
    private String typeName;

    @Min(value = 2, message = "Count players must be at least 2")
    private int countPlayers;

    @Size(max = 500, message = "Summary cannot exceed 500 characters")
    private String summary;
}
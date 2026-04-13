package org.example.hobbycatalog.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TypeHobbiesDTO {

    private Long idType;

    @NotNull(message = "Type name is required")
    @NotEmpty(message = "Type name cannot be empty")
    private String typeName;

    @NotNull(message = "Count players is required")
    @Min(value = 2, message = "Count players must be at least 2")
    private int countPlayers;

    @NotNull(message = "Summary is required")
    @NotEmpty(message = "Summary cannot be empty")
    private String summary;
}
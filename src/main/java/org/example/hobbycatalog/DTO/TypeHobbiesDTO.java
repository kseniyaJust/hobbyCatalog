package org.example.hobbycatalog.DTO;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.example.hobbycatalog.entity.UsersInfo;

@Data
public class TypeHobbiesDTO {

    private Long idType;

    @NotEmpty
    private String typeName;

    @Min(2)
    private int countPlayers;

    @NotEmpty
    private String summary;

}

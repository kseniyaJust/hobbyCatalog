package org.example.hobbycatalog.DTO;

import lombok.Data;

@Data
public class UpdateTypeHobbyDTO {

    private String typeName;

    private int countPlayers;

    private String summary;
}

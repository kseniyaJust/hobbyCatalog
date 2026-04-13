package org.example.hobbycatalog.DTO;

import lombok.Data;

@Data
public class ModifyCartDTO {
    private Long hobbyId;
    private int quantity;
}

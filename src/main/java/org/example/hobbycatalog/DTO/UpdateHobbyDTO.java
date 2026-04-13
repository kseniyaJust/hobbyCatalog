package org.example.hobbycatalog.DTO;

import lombok.Data;
import org.example.hobbycatalog.entity.TypeHobbies;


@Data
public class UpdateHobbyDTO {

    private String nameType;

    private String name;

    private String creator;

    private double price;
}

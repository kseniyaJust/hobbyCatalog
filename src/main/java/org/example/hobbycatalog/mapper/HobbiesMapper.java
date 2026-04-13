package org.example.hobbycatalog.mapper;

import org.example.hobbycatalog.DTO.HobbyDTO;
import org.example.hobbycatalog.entity.Hobbies;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HobbiesMapper {
    @Mapping(target = "typeName", source = "typeAndHobbies.typeName")
    HobbyDTO toDTO(Hobbies hobbies);

    @Mapping(target = "idHobby", ignore = true)
    Hobbies toEntity(HobbyDTO hobbyDTO);
}
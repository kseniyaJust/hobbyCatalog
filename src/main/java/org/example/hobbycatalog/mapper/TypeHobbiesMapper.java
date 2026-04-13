package org.example.hobbycatalog.mapper;

import org.example.hobbycatalog.DTO.TypeHobbiesDTO;
import org.example.hobbycatalog.entity.TypeHobbies;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TypeHobbiesMapper {
    // Entity to DTO
    TypeHobbiesDTO toDTO(TypeHobbies typeHobbies);


    @Mapping(target = "idType",ignore = true)
    TypeHobbies toEntity(TypeHobbiesDTO typeHobbiesDTO);
}

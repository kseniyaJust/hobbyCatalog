package org.example.hobbycatalog.mapper;

import org.example.hobbycatalog.DTO.HobbyDTO;
import org.example.hobbycatalog.DTO.UserAddressDTO;
import org.example.hobbycatalog.entity.Hobbies;
import org.example.hobbycatalog.entity.UserAdress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    UserAddressDTO toDTO(UserAdress userAdress);

    @Mapping(target = "id_adress", ignore = true)
    UserAdress toEntity(UserAddressDTO userAddressDTO);
}

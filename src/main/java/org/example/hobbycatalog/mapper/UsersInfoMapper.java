package org.example.hobbycatalog.mapper;

import org.example.hobbycatalog.DTO.AuthRequestDTO;
import org.example.hobbycatalog.DTO.AuthResponseDTO;
import org.example.hobbycatalog.DTO.RegistUserDTO;
import org.example.hobbycatalog.entity.UsersInfo;
import org.mapstruct.Mapper;

@Mapper
public interface UsersInfoMapper {

    AuthResponseDTO toDTO(AuthRequestDTO usersInfo);

    RegistUserDTO toDTO(UsersInfo usersInfo);


    UsersInfo toEntity(AuthRequestDTO usersInfoDTO);

    UsersInfo toEntityRegist(RegistUserDTO usersInfoDTO);

}

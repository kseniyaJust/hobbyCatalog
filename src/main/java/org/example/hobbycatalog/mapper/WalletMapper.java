package org.example.hobbycatalog.mapper;

import org.example.hobbycatalog.DTO.WalletDTO;
import org.example.hobbycatalog.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface WalletMapper {
    WalletDTO toDTO(Wallet wallet);

    // DTO to Entity
    @Mappings({
            @Mapping(target = "idWallet", ignore = true),
            @Mapping(target = "usersInfo", ignore = true)
    })
    Wallet toEntity(WalletDTO walletDTO);
}

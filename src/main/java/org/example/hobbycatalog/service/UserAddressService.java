package org.example.hobbycatalog.service;

import org.example.hobbycatalog.DTO.UpdateUserAddressDTO;
import org.example.hobbycatalog.DTO.UserAddressDTO;
import org.example.hobbycatalog.entity.Hobbies;
import org.example.hobbycatalog.entity.TypeHobbies;
import org.example.hobbycatalog.entity.UserAdress;
import org.example.hobbycatalog.exceptions.ItemNotFoundException;
import org.example.hobbycatalog.mapper.UserAddressMapper;
import org.example.hobbycatalog.repository.UserAdressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressService {
    UserAdressRepository userAdressRepository;
    UserAddressMapper userAddressMapper;

    public UserAddressService(UserAdressRepository userAdressRepository,  UserAddressMapper userAddressMapper) {
        this.userAdressRepository = userAdressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    public List<UserAdress> getAllUserAdresses(){
        return userAdressRepository.findAll();
    }

    public UserAddressDTO createNewAddress(UserAddressDTO userAddressDTO){

        UserAdress address = userAddressMapper.toEntity(userAddressDTO);
        UserAdress savedAddress = userAdressRepository.save(address);

        return userAddressMapper.toDTO(savedAddress);
    }
    public UserAddressDTO updateAddress(Long id, UpdateUserAddressDTO userAddressDTO){
        UserAdress existingAddress = userAdressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        if(userAddressDTO.getCity() != null){
            existingAddress.setCity(userAddressDTO.getCity());
        }
        if(userAddressDTO.getStreet() != null)
            existingAddress.setStreet(userAddressDTO.getStreet());
        if(userAddressDTO.getNumber_flat() > 0)
            existingAddress.setNumber_flat(userAddressDTO.getNumber_flat());
        if(userAddressDTO.getNumber_home() >0){
            existingAddress.setNumber_home(userAddressDTO.getNumber_home());
        }

        UserAdress updatedType = userAdressRepository.save(existingAddress);

        return userAddressMapper.toDTO(updatedType);

    }
    public String deleteAddress(Long id) {
        if(userAdressRepository.findById(id).isPresent()){
            userAdressRepository.deleteById(id);
            return "Address with id: " + id + " deleted";
        }
        throw new ItemNotFoundException("Address with id: " + id + " not found");
    }

    public UserAdress getOneAddress(Long id){
        if(userAdressRepository.findById(id).isPresent())
            return userAdressRepository.findById(id).get();
        throw new ItemNotFoundException("Address with id: " + id +" not found");
    }

}

package org.example.hobbycatalog.controller;

import org.example.hobbycatalog.DTO.UpdateUserAddressDTO;
import org.example.hobbycatalog.DTO.UserAddressDTO;
import org.example.hobbycatalog.entity.UserAdress;
import org.example.hobbycatalog.service.UserAddressService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/users/adress")
//acces only to current user
public class UserAdressController {

    UserAddressService userAddressService;

    public UserAdressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @GetMapping
    public List<UserAdress> getAllAdresses(){

        return userAddressService.getAllUserAdresses();
    }
    @GetMapping("/{id}")
    public UserAdress getOneAddress(@PathVariable Long id){
        return userAddressService.getOneAddress(id);
    }

    @PostMapping
    public UserAddressDTO addAdress(@RequestBody UserAddressDTO userAddress){
        return userAddressService.createNewAddress(userAddress);
    }

    @PutMapping("/{id}")
    public UserAddressDTO updateAdress(@PathVariable Long id, @RequestBody UpdateUserAddressDTO userAddress){

        return userAddressService.updateAddress(id, userAddress);
    }

    @DeleteMapping("/{id}")
    public String deleteAdress(@PathVariable Long id){

        return userAddressService.deleteAddress(id);
    }
}

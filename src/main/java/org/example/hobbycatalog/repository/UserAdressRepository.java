package org.example.hobbycatalog.repository;

import org.example.hobbycatalog.DTO.UserAddressDTO;
import org.example.hobbycatalog.entity.UserAdress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdressRepository extends JpaRepository<UserAdress,Long> {
}

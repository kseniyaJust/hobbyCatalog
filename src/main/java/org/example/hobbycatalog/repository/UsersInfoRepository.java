package org.example.hobbycatalog.repository;

import org.example.hobbycatalog.entity.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersInfoRepository extends JpaRepository<UsersInfo,Long> {
    Optional<UsersInfo> findByEmail(String email);
    boolean existsByEmail(String email);
}

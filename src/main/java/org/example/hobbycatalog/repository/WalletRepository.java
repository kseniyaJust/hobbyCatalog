package org.example.hobbycatalog.repository;

import org.example.hobbycatalog.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    List<Wallet> findByUsersInfo_IdUser(Long userId);

    Optional<Wallet> findByIdWalletAndUsersInfo_IdUser(Long walletId, Long userId);

    boolean existsByIdWalletAndUsersInfo_IdUser(Long walletId, Long userId);
}

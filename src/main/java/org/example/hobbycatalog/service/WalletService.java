package org.example.hobbycatalog.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hobbycatalog.DTO.WalletDTO;
import org.example.hobbycatalog.entity.UsersInfo;
import org.example.hobbycatalog.entity.Wallet;
import org.example.hobbycatalog.exceptions.ItemNotFoundException;
import org.example.hobbycatalog.mapper.WalletMapper;
import org.example.hobbycatalog.repository.UsersInfoRepository;
import org.example.hobbycatalog.repository.WalletRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UsersInfoRepository usersInfoRepository;
    private final WalletMapper walletMapper;

    public WalletService(WalletRepository walletRepository,
                         UsersInfoRepository usersInfoRepository,
                         WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.usersInfoRepository = usersInfoRepository;
        this.walletMapper = walletMapper;
    }

    private UsersInfo getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return usersInfoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<WalletDTO> getAllUserWallets() {
        UsersInfo currentUser = getCurrentUser();

        return walletRepository.findByUsersInfo_IdUser(currentUser.getIdUser())
                .stream()
                .map(walletMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WalletDTO getWalletById(Long walletId) {
        UsersInfo currentUser = getCurrentUser();

        Wallet wallet = walletRepository.findByIdWalletAndUsersInfo_IdUser(walletId, currentUser.getIdUser())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Wallet with id " + walletId + " not found or doesn't belong to you"
                ));

        return walletMapper.toDTO(wallet);
    }

    @Transactional
    public WalletDTO createWallet(WalletDTO walletDTO) {
        UsersInfo currentUser = getCurrentUser();

        boolean exists = walletRepository.findAll()
                .stream()
                .anyMatch(w -> w.getCart_number().equals(walletDTO.getCart_number())
                        && w.getUsersInfo().getIdUser().equals(currentUser.getIdUser()));

        if (exists) {
            throw new RuntimeException("Wallet with this card number already exists for you");
        }

        Wallet wallet = walletMapper.toEntity(walletDTO);
        wallet.setUsersInfo(currentUser);

        Wallet savedWallet = walletRepository.save(wallet);
        log.info("Created new wallet with id {} for user {}", savedWallet.getIdWallet(), currentUser.getEmail());

        return walletMapper.toDTO(savedWallet);
    }

    @Transactional
    public WalletDTO updateWallet(Long walletId, WalletDTO walletDTO) {
        UsersInfo currentUser = getCurrentUser();

        Wallet existingWallet = walletRepository.findByIdWalletAndUsersInfo_IdUser(walletId, currentUser.getIdUser())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Wallet with id " + walletId + " not found or doesn't belong to you"
                ));

        existingWallet.setOwner_name(walletDTO.getOwner_name());
        existingWallet.setCart_number(walletDTO.getCart_number());
        existingWallet.setDate_expire(walletDTO.getDate_expire());
        existingWallet.setCVC(walletDTO.getCVC());

        Wallet updatedWallet = walletRepository.save(existingWallet);
        log.info("Updated wallet with id {} for user {}", updatedWallet.getIdWallet(), currentUser.getEmail());

        return walletMapper.toDTO(updatedWallet);
    }

    @Transactional
    public void deleteWallet(Long walletId) {
        UsersInfo currentUser = getCurrentUser();

        if (!walletRepository.existsByIdWalletAndUsersInfo_IdUser(walletId, currentUser.getIdUser())) {
            throw new ItemNotFoundException(
                    "Wallet with id " + walletId + " not found or doesn't belong to you"
            );
        }

        walletRepository.deleteById(walletId);
        log.info("Deleted wallet with id {} for user {}", walletId, currentUser.getEmail());
    }

    @Transactional(readOnly = true)
    public int getTotalBalance() {
        UsersInfo currentUser = getCurrentUser();
        return currentUser.getBalance_amount();
    }

    @Transactional
    public String addMoney(Long walletId, int amount) {
        UsersInfo currentUser = getCurrentUser();

        Wallet wallet = walletRepository.findByIdWalletAndUsersInfo_IdUser(walletId, currentUser.getIdUser())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Wallet with id " + walletId + " not found or doesn't belong to you"
                ));

        currentUser.setBalance_amount(currentUser.getBalance_amount() + amount);
        usersInfoRepository.save(currentUser);


        return "Successfully added " + amount + " to balance. Current balance: " + currentUser.getBalance_amount();
    }
}
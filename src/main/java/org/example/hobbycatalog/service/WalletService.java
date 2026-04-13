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

    // Получить текущего авторизованного пользователя
    private UsersInfo getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return usersInfoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Получить все кошельки текущего пользователя
    @Transactional(readOnly = true)
    public List<WalletDTO> getAllUserWallets() {
        UsersInfo currentUser = getCurrentUser();

        return walletRepository.findByUsersInfo_IdUser(currentUser.getIdUser())
                .stream()
                .map(walletMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Получить конкретный кошелек по ID (только если он принадлежит пользователю)
    @Transactional(readOnly = true)
    public WalletDTO getWalletById(Long walletId) {
        UsersInfo currentUser = getCurrentUser();

        Wallet wallet = walletRepository.findByIdWalletAndUsersInfo_IdUser(walletId, currentUser.getIdUser())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Wallet with id " + walletId + " not found or doesn't belong to you"
                ));

        return walletMapper.toDTO(wallet);
    }

    // Создать новый кошелек
    @Transactional
    public WalletDTO createWallet(WalletDTO walletDTO) {
        UsersInfo currentUser = getCurrentUser();

        // Проверка: есть ли уже кошелек с таким номером у пользователя
        boolean exists = walletRepository.findAll()
                .stream()
                .anyMatch(w -> w.getCart_number().equals(walletDTO.getCart_number())
                        && w.getUsersInfo().getIdUser().equals(currentUser.getIdUser()));

        if (exists) {
            throw new RuntimeException("Wallet with this card number already exists for you");
        }

        // Создаем новый кошелек
        Wallet wallet = walletMapper.toEntity(walletDTO);
        wallet.setUsersInfo(currentUser);

        Wallet savedWallet = walletRepository.save(wallet);
        log.info("Created new wallet with id {} for user {}", savedWallet.getIdWallet(), currentUser.getEmail());

        return walletMapper.toDTO(savedWallet);
    }

    // Обновить кошелек (только если он принадлежит пользователю)
    @Transactional
    public WalletDTO updateWallet(Long walletId, WalletDTO walletDTO) {
        UsersInfo currentUser = getCurrentUser();

        // Проверяем, что кошелек принадлежит пользователю
        Wallet existingWallet = walletRepository.findByIdWalletAndUsersInfo_IdUser(walletId, currentUser.getIdUser())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Wallet with id " + walletId + " not found or doesn't belong to you"
                ));

        // Обновляем поля
        existingWallet.setOwner_name(walletDTO.getOwner_name());
        existingWallet.setCart_number(walletDTO.getCart_number());
        existingWallet.setDate_expire(walletDTO.getDate_expire());
        existingWallet.setCVC(walletDTO.getCVC());

        Wallet updatedWallet = walletRepository.save(existingWallet);
        log.info("Updated wallet with id {} for user {}", updatedWallet.getIdWallet(), currentUser.getEmail());

        return walletMapper.toDTO(updatedWallet);
    }

    // Удалить кошелек (только если он принадлежит пользователю)
    @Transactional
    public void deleteWallet(Long walletId) {
        UsersInfo currentUser = getCurrentUser();

        // Проверяем, что кошелек принадлежит пользователю
        if (!walletRepository.existsByIdWalletAndUsersInfo_IdUser(walletId, currentUser.getIdUser())) {
            throw new ItemNotFoundException(
                    "Wallet with id " + walletId + " not found or doesn't belong to you"
            );
        }

        walletRepository.deleteById(walletId);
        log.info("Deleted wallet with id {} for user {}", walletId, currentUser.getEmail());
    }

    // Получить баланс всех кошельков пользователя (суммарно)
    @Transactional(readOnly = true)
    public int getTotalBalance() {
        UsersInfo currentUser = getCurrentUser();
        return currentUser.getBalance_amount();
    }

    // Пополнить баланс пользователя
    @Transactional
    public String addMoney(Long walletId, int amount) {
        UsersInfo currentUser = getCurrentUser();

        // Проверяем, что кошелек принадлежит пользователю
        Wallet wallet = walletRepository.findByIdWalletAndUsersInfo_IdUser(walletId, currentUser.getIdUser())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Wallet with id " + walletId + " not found or doesn't belong to you"
                ));

        // Обновляем баланс пользователя
        currentUser.setBalance_amount(currentUser.getBalance_amount() + amount);
        usersInfoRepository.save(currentUser);


        return "Successfully added " + amount + " to balance. Current balance: " + currentUser.getBalance_amount();
    }
}
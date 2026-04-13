package org.example.hobbycatalog.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hobbycatalog.DTO.BalanceDTO;
import org.example.hobbycatalog.DTO.RegistUserDTO;
import org.example.hobbycatalog.DTO.TopUpBalanceDTO;
import org.example.hobbycatalog.DTO.UpdateUserDTO;
import org.example.hobbycatalog.entity.UsersInfo;
import org.example.hobbycatalog.entity.Wallet;
import org.example.hobbycatalog.enumpackage.Role;
import org.example.hobbycatalog.exceptions.ConflictException;
import org.example.hobbycatalog.exceptions.ItemNotFoundException;
import org.example.hobbycatalog.mapper.UsersInfoMapper;
import org.example.hobbycatalog.repository.UsersInfoRepository;
import org.example.hobbycatalog.repository.WalletRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
public class UsersInfoService implements UserDetailsService {

    private final UsersInfoRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersInfoMapper userMapper;
    private final WalletRepository walletRepository;

    public UsersInfoService(UsersInfoRepository userRepository,
                            UsersInfoMapper userMapper,
                            PasswordEncoder passwordEncoder,
                            WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.walletRepository = walletRepository;
    }

    public String register(RegistUserDTO registerRequest) {
        checkEmailExists(registerRequest.getEmail());

        UsersInfo userInfo = userMapper.toEntityRegist(registerRequest);
        userInfo.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userInfo.setRole(Role.USER);

        userRepository.save(userInfo);
        log.info("Registered new user: {}", registerRequest.getEmail());

        return "You were registered. Log in your account";
    }

    public UsersInfo getUserByEmail(String email) {
        log.info("Find by email in users info: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ItemNotFoundException("User not found with email: " + email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", email);
        UsersInfo user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }

    private void checkEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("User with email " + email + " already exists");
        }
    }

    private UsersInfo getCurrentUser() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            String email = userDetails.getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            log.error("Error getting current user: {}", e.getMessage());
            throw new RuntimeException("User not authenticated");
        }
    }

    @Transactional
    public UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO) {
        UsersInfo user = getCurrentUser();

        if (updateUserDTO.getName() != null && !updateUserDTO.getName().isEmpty()) {
            user.setName(updateUserDTO.getName());
        }

        if (updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(updateUserDTO.getEmail()) &&
                    !user.getEmail().equals(updateUserDTO.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(updateUserDTO.getEmail());
        }

        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        userRepository.save(user);
        log.info("Updated user info for {}", user.getEmail());

        return updateUserDTO;
    }

    @Transactional
    public BalanceDTO topUpBalance(TopUpBalanceDTO topUpBalanceDTO) {
        UsersInfo user = getCurrentUser();

        Wallet wallet = walletRepository.findByIdWalletAndUsersInfo_IdUser(
                        topUpBalanceDTO.getWalletId(), user.getIdUser())
                .orElseThrow(() -> new RuntimeException("Wallet not found or doesn't belong to you"));

        int oldBalance = user.getBalance_amount();
        user.setBalance_amount(oldBalance + topUpBalanceDTO.getAmount());
        userRepository.save(user);

        log.info("User {} topped up balance by {} using wallet {}. Old balance: {}, New balance: {}",
                user.getEmail(), topUpBalanceDTO.getAmount(), wallet.getIdWallet(),
                oldBalance, user.getBalance_amount());

        return new BalanceDTO(
                user.getBalance_amount(),
                "Successfully added " + topUpBalanceDTO.getAmount() + " to balance"
        );
    }

    @Transactional(readOnly = true)
    public BalanceDTO getBalance() {
        UsersInfo user = getCurrentUser();
        return new BalanceDTO(
                user.getBalance_amount(),
                "Current balance: " + user.getBalance_amount()
        );
    }

    @Transactional(readOnly = true)
    public UsersInfo getUserInfo() {
        return getCurrentUser();
    }
}
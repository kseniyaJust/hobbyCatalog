package org.example.hobbycatalog.service;

import org.example.hobbycatalog.DTO.*;
import org.example.hobbycatalog.entity.UsersInfo;
import org.example.hobbycatalog.entity.Wallet;
import org.example.hobbycatalog.enumpackage.Role;
import org.example.hobbycatalog.exceptions.ConflictException;
import org.example.hobbycatalog.mapper.UsersInfoMapper;
import org.example.hobbycatalog.repository.UsersInfoRepository;
import org.example.hobbycatalog.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersInfoServiceTest {

    @Mock
    private UsersInfoRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsersInfoMapper userMapper;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UsersInfoService usersInfoService;

    private UsersInfo testUser;
    private RegistUserDTO registUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new UsersInfo();
        testUser.setIdUser(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setName("Test User");
        testUser.setRole(Role.USER);
        testUser.setBalance_amount(1000);

        registUserDTO = new RegistUserDTO();
        registUserDTO.setEmail("test@example.com");
        registUserDTO.setPassword("password123");
        registUserDTO.setName("Test User");

        // Настройка SecurityContext для getCurrentUser()
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                org.springframework.security.core.userdetails.User
                        .withUsername("test@example.com")
                        .password("password")
                        .authorities(java.util.Collections.emptyList())
                        .build()
        );
    }

    @Test
    void register_Success() {
        when(userRepository.findByEmail(registUserDTO.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntityRegist(registUserDTO)).thenReturn(testUser);
        when(passwordEncoder.encode(registUserDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UsersInfo.class))).thenReturn(testUser);

        String result = usersInfoService.register(registUserDTO);

        assertEquals("You were registered. Log in your account", result);
        verify(userRepository, times(1)).save(any(UsersInfo.class));
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        when(userRepository.findByEmail(registUserDTO.getEmail())).thenReturn(Optional.of(testUser));

        assertThrows(ConflictException.class, () -> usersInfoService.register(registUserDTO));
        verify(userRepository, never()).save(any(UsersInfo.class));
    }

    @Test
    void getUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UsersInfo found = usersInfoService.getUserByEmail("test@example.com");

        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    void loadUserByUsername_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = usersInfoService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    void getBalance_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        BalanceDTO balance = usersInfoService.getBalance();

        assertNotNull(balance);
        assertEquals(1000, balance.getCurrentBalance());
    }

    @Test
    void topUpBalance_Success() {
        TopUpBalanceDTO topUpDTO = new TopUpBalanceDTO();
        topUpDTO.setWalletId(1L);
        topUpDTO.setAmount(500);

        Wallet wallet = new Wallet();
        wallet.setIdWallet(1L);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(walletRepository.findByIdWalletAndUsersInfo_IdUser(1L, 1L)).thenReturn(Optional.of(wallet));
        when(userRepository.save(any(UsersInfo.class))).thenReturn(testUser);

        BalanceDTO result = usersInfoService.topUpBalance(topUpDTO);

        assertNotNull(result);
        assertEquals(1500, result.getCurrentBalance());
        verify(userRepository, times(1)).save(any(UsersInfo.class));
    }
}
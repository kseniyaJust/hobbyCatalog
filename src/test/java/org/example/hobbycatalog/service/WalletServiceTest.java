package org.example.hobbycatalog.service;

import org.example.hobbycatalog.DTO.WalletDTO;
import org.example.hobbycatalog.entity.UsersInfo;
import org.example.hobbycatalog.entity.Wallet;
import org.example.hobbycatalog.mapper.WalletMapper;
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

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UsersInfoRepository usersInfoRepository;

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private WalletService walletService;

    private UsersInfo testUser;
    private Wallet testWallet;
    private WalletDTO testWalletDTO;

    @BeforeEach
    void setUp() {
        testUser = new UsersInfo();
        testUser.setIdUser(1L);
        testUser.setEmail("test@example.com");
        testUser.setBalance_amount(1000);

        testWallet = new Wallet();
        testWallet.setIdWallet(1L);
        testWallet.setOwner_name("Test Owner");
        testWallet.setCart_number(1234567890123456L);
        testWallet.setCVC(123L);
        testWallet.setUsersInfo(testUser);

        testWalletDTO = new WalletDTO();
        testWalletDTO.setOwner_name("Test Owner");
        testWalletDTO.setCart_number(1234567890123456L);
        testWalletDTO.setCVC(123L);

        // Настройка SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(
                org.springframework.security.core.userdetails.User
                        .withUsername("test@example.com")
                        .password("password")
                        .authorities(java.util.Collections.emptyList())
                        .build()
        );
        when(usersInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
    }

    @Test
    void getAllUserWallets_Success() {
        when(walletRepository.findByUsersInfo_IdUser(1L)).thenReturn(Arrays.asList(testWallet));
        when(walletMapper.toDTO(any(Wallet.class))).thenReturn(testWalletDTO);

        List<WalletDTO> result = walletService.getAllUserWallets();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getWalletById_Success() {
        when(walletRepository.findByIdWalletAndUsersInfo_IdUser(1L, 1L)).thenReturn(Optional.of(testWallet));
        when(walletMapper.toDTO(testWallet)).thenReturn(testWalletDTO);

        WalletDTO result = walletService.getWalletById(1L);

        assertNotNull(result);
        assertEquals("Test Owner", result.getOwner_name());
    }

    @Test
    void getWalletById_NotFound_ThrowsException() {
        when(walletRepository.findByIdWalletAndUsersInfo_IdUser(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> walletService.getWalletById(1L));
    }

    @Test
    void createWallet_Success() {
        when(walletRepository.findAll()).thenReturn(Arrays.asList());
        when(walletMapper.toEntity(testWalletDTO)).thenReturn(testWallet);
        when(walletRepository.save(any(Wallet.class))).thenReturn(testWallet);
        when(walletMapper.toDTO(testWallet)).thenReturn(testWalletDTO);

        WalletDTO result = walletService.createWallet(testWalletDTO);

        assertNotNull(result);
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void updateWallet_Success() {
        when(walletRepository.findByIdWalletAndUsersInfo_IdUser(1L, 1L)).thenReturn(Optional.of(testWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(testWallet);
        when(walletMapper.toDTO(testWallet)).thenReturn(testWalletDTO);

        WalletDTO result = walletService.updateWallet(1L, testWalletDTO);

        assertNotNull(result);
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void deleteWallet_Success() {
        when(walletRepository.existsByIdWalletAndUsersInfo_IdUser(1L, 1L)).thenReturn(true);
        doNothing().when(walletRepository).deleteById(1L);

        assertDoesNotThrow(() -> walletService.deleteWallet(1L));
        verify(walletRepository, times(1)).deleteById(1L);
    }

    @Test
    void addMoney_Success() {
        when(walletRepository.findByIdWalletAndUsersInfo_IdUser(1L, 1L)).thenReturn(Optional.of(testWallet));
        when(usersInfoRepository.save(any(UsersInfo.class))).thenReturn(testUser);

        String result = walletService.addMoney(1L, 500);

        assertNotNull(result);
        assertTrue(result.contains("Successfully added 500"));
        assertEquals(1500, testUser.getBalance_amount());
    }
}
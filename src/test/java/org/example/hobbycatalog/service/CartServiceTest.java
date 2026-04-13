package org.example.hobbycatalog.service;

import org.example.hobbycatalog.DTO.*;
import org.example.hobbycatalog.entity.*;
import org.example.hobbycatalog.mapper.HobbiesMapper;
import org.example.hobbycatalog.repository.CartRepository;
import org.example.hobbycatalog.repository.HobbiesRepository;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private HobbiesRepository hobbiesRepository;

    @Mock
    private UsersInfoRepository usersInfoRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private HobbiesMapper hobbiesMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CartService cartService;

    private UsersInfo testUser;
    private Cart testCart;
    private Hobbies testHobby;

    @BeforeEach
    void setUp() {
        testUser = new UsersInfo();
        testUser.setIdUser(1L);
        testUser.setEmail("test@example.com");
        testUser.setBalance_amount(1000);

        testCart = new Cart();
        testCart.setId_cart(1L);
        testCart.setHobbies(new HashSet<>());
        testCart.setAmount(0);
        testCart.setUsersInfo(Set.of(testUser));

        testHobby = new Hobbies();
        testHobby.setIdHobby(1L);
        testHobby.setName("Test Hobby");
        testHobby.setPrice(100);
        testHobby.setCreator("Test Creator");

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
    void getCart_WhenCartExists_ReturnsCart() {
        when(cartRepository.findByUsersInfo_IdUser(1L)).thenReturn(Optional.of(testCart));

        CartDTO result = cartService.getCart();

        assertNotNull(result);
        assertEquals(0, result.getAmount());
    }

    @Test
    void getCart_WhenCartNotExists_CreatesNewCart() {
        when(cartRepository.findByUsersInfo_IdUser(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartDTO result = cartService.getCart();

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void addItemToCart_Success() {
        when(cartRepository.findByUsersInfo_IdUser(1L)).thenReturn(Optional.of(testCart));
        when(hobbiesRepository.findById(1L)).thenReturn(Optional.of(testHobby));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartDTO result = cartService.addItemToCart(1L, 1);

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void deleteItemFromCart_Success() {
        testCart.getHobbies().add(testHobby);
        testCart.setAmount(100);

        when(cartRepository.findByUsersInfo_IdUser(1L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartDTO result = cartService.deleteItemFromCart(1L);

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void purchaseItems_Success() {
        testCart.getHobbies().add(testHobby);
        testCart.setAmount(100);

        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setPurchaseAll(true);

        when(cartRepository.findByUsersInfo_IdUser(1L)).thenReturn(Optional.of(testCart));
        when(usersInfoRepository.save(any(UsersInfo.class))).thenReturn(testUser);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
        when(hobbiesMapper.toDTO(any(Hobbies.class))).thenReturn(new HobbyDTO());

        PurchaseResponseDTO result = cartService.purchaseItems(purchaseDTO);

        assertNotNull(result);
        assertEquals("Purchase successful!", result.getMessage());
        assertEquals(900, result.getRemainingBalance()); // 1000 - 100
    }

    @Test
    void purchaseItems_InsufficientBalance_ThrowsException() {
        testUser.setBalance_amount(50);
        testCart.getHobbies().add(testHobby);
        testCart.setAmount(100);

        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setPurchaseAll(true);

        when(cartRepository.findByUsersInfo_IdUser(1L)).thenReturn(Optional.of(testCart));

        assertThrows(RuntimeException.class, () -> cartService.purchaseItems(purchaseDTO));
    }
}
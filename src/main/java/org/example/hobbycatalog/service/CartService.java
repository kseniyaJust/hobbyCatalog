package org.example.hobbycatalog.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hobbycatalog.DTO.*;
import org.example.hobbycatalog.entity.*;
import org.example.hobbycatalog.exceptions.ItemNotFoundException;
import org.example.hobbycatalog.mapper.HobbiesMapper;
import org.example.hobbycatalog.repository.CartRepository;
import org.example.hobbycatalog.repository.HobbiesRepository;
import org.example.hobbycatalog.repository.UsersInfoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final HobbiesRepository hobbiesRepository;
    private final UsersInfoRepository usersInfoRepository;
    private final HobbiesMapper hobbiesMapper;

    public CartService(CartRepository cartRepository,
                       HobbiesRepository hobbiesRepository,
                       UsersInfoRepository usersInfoRepository,
                       HobbiesMapper hobbiesMapper) {
        this.cartRepository = cartRepository;
        this.hobbiesRepository = hobbiesRepository;
        this.usersInfoRepository = usersInfoRepository;
        this.hobbiesMapper = hobbiesMapper;
    }

    private UsersInfo getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return usersInfoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Просмотр всех товаров в корзине
    @Transactional(readOnly = true)
    public CartDTO getCart() {
        UsersInfo user = getCurrentUser();
        List<Cart> cartItems = cartRepository.findByUserId(user.getIdUser());

        CartDTO cartDTO = new CartDTO();
        List<CartItemDTO> items = new ArrayList<>();
        int totalAmount = 0;

        for (Cart item : cartItems) {
            Hobbies hobby = item.getHobby();
            double itemTotalPrice = hobby.getPrice() * item.getQuantity();
            totalAmount += itemTotalPrice;

            CartItemDTO itemDTO = new CartItemDTO(
                    hobby.getName(),
                    item.getQuantity(),
                    hobby.getPrice(),
                    itemTotalPrice
            );
            items.add(itemDTO);
        }

        cartDTO.setItems(items);
        cartDTO.setTotalAmount(totalAmount);

        return cartDTO;
    }

    // Добавление товара в корзину
    @Transactional
    public CartDTO addItemToCart(Long hobbyId, int quantity) {
        UsersInfo user = getCurrentUser();

        Hobbies hobby = hobbiesRepository.findById(hobbyId)
                .orElseThrow(() -> new ItemNotFoundException("Hobby not found with id: " + hobbyId));

        // Проверяем, есть ли уже такой товар в корзине
        java.util.Optional<Cart> existingItem = cartRepository.findByUserIdAndHobbyId(user.getIdUser(), hobbyId);

        if (existingItem.isPresent()) {
            // Если есть, обновляем количество
            Cart item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartRepository.save(item);
            log.info("Updated quantity of hobby {} in cart for user {}", hobby.getName(), user.getEmail());
        } else {
            // Если нет, создаем новую запись
            Cart cartItem = new Cart();
            cartItem.setUser(user);
            cartItem.setHobby(hobby);
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
            log.info("Added hobby {} to cart for user {}", hobby.getName(), user.getEmail());
        }

        return getCart();
    }

    // Обновление количества товара
    @Transactional
    public CartDTO updateItemQuantity(Long hobbyId, int quantity) {
        UsersInfo user = getCurrentUser();

        Cart cartItem = cartRepository.findByUserIdAndHobbyId(user.getIdUser(), hobbyId)
                .orElseThrow(() -> new ItemNotFoundException("Hobby not found in cart"));

        if (quantity <= 0) {
            cartRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
        }

        log.info("Updated quantity of hobby {} to {} for user {}", hobbyId, quantity, user.getEmail());
        return getCart();
    }

    // Удаление товара из корзины
    @Transactional
    public CartDTO deleteItemFromCart(Long hobbyId) {
        UsersInfo user = getCurrentUser();
        cartRepository.deleteByUserIdAndHobbyId(user.getIdUser(), hobbyId);
        log.info("Removed hobby {} from cart for user {}", hobbyId, user.getEmail());
        return getCart();
    }

    // Очистка корзины
    @Transactional
    public CartDTO clearCart() {
        UsersInfo user = getCurrentUser();
        cartRepository.clearCart(user.getIdUser());
        log.info("Cleared cart for user {}", user.getEmail());
        return getCart();
    }

    // Покупка товаров
    @Transactional
    public PurchaseResponseDTO purchaseItems(PurchaseDTO purchaseDTO) {
        UsersInfo user = getCurrentUser();
        List<Cart> cartItems = cartRepository.findByUserId(user.getIdUser());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Nothing to purchase.");
        }

        List<Hobbies> itemsToPurchase = new ArrayList<>();
        int totalAmount = 0;

        if (purchaseDTO.isPurchaseAll() || purchaseDTO.getHobbyIds() == null || purchaseDTO.getHobbyIds().isEmpty()) {
            for (Cart item : cartItems) {
                itemsToPurchase.add(item.getHobby());
                totalAmount += (int) (item.getHobby().getPrice() * item.getQuantity());
            }
            log.info("Purchasing all items. Total: {}", totalAmount);
        } else {
            for (Long hobbyId : purchaseDTO.getHobbyIds()) {
                Cart cartItem = cartItems.stream()
                        .filter(item -> item.getHobby().getIdHobby().equals(hobbyId))
                        .findFirst()
                        .orElseThrow(() -> new ItemNotFoundException("Hobby with id " + hobbyId + " not found in cart"));
                itemsToPurchase.add(cartItem.getHobby());
                totalAmount += (int) (cartItem.getHobby().getPrice() * cartItem.getQuantity());
            }
            log.info("Purchasing selected items: {}. Total: {}", purchaseDTO.getHobbyIds(), totalAmount);
        }

        if (user.getBalance_amount() < totalAmount) {
            throw new RuntimeException("Insufficient balance. Need: " + totalAmount +
                    ", Available: " + user.getBalance_amount());
        }

        // Списываем средства
        user.setBalance_amount(user.getBalance_amount() - totalAmount);
        usersInfoRepository.save(user);

        // Очищаем корзину
        cartRepository.clearCart(user.getIdUser());

        List<HobbyDTO> purchasedItems = itemsToPurchase.stream()
                .map(hobbiesMapper::toDTO)
                .collect(Collectors.toList());

        log.info("User {} purchased items for {}. Remaining balance: {}",
                user.getEmail(), totalAmount, user.getBalance_amount());

        return new PurchaseResponseDTO(
                "Purchase successful!",
                totalAmount,
                user.getBalance_amount(),
                purchasedItems
        );
    }
}
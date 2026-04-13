package org.example.hobbycatalog.repository;

import org.example.hobbycatalog.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Найти все товары в корзине пользователя
    @Query("SELECT c FROM Cart c WHERE c.user.idUser = :userId")
    List<Cart> findByUserId(@Param("userId") Long userId);

    // Найти конкретный товар в корзине пользователя
    @Query("SELECT c FROM Cart c WHERE c.user.idUser = :userId AND c.hobby.idHobby = :hobbyId")
    Optional<Cart> findByUserIdAndHobbyId(@Param("userId") Long userId, @Param("hobbyId") Long hobbyId);

    // Удалить товар из корзины
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.idUser = :userId AND c.hobby.idHobby = :hobbyId")
    void deleteByUserIdAndHobbyId(@Param("userId") Long userId, @Param("hobbyId") Long hobbyId);

    // Очистить всю корзину пользователя
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.idUser = :userId")
    void clearCart(@Param("userId") Long userId);

    // Подсчитать количество товаров в корзине
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.user.idUser = :userId")
    int countByUserId(@Param("userId") Long userId);
}
package org.example.hobbycatalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.example.hobbycatalog.enumpackage.Role;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users_info")
public class UsersInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "VARCHAR(10)")
    private Role role;

    @Column(name = "balance_amount")
    private int balance_amount;

    @OneToMany(mappedBy = "usersInfo")
    @JsonIgnore
    private Set<Wallet> wallets;

    // Связь с корзиной (один пользователь - много товаров в корзине)
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Cart> cartItems;

    @OneToMany(mappedBy = "usersInfo_adress")
    @JsonIgnore
    private Set<UserAdress> userAdresses;

    @OneToMany(mappedBy = "creatorUser")
    @JsonIgnore
    private List<TypeHobbies> creatorType;
}
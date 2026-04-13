package org.example.hobbycatalog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart")
    private Long id_cart;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersInfo user;

    @ManyToOne
    @JoinColumn(name = "hobby_id", nullable = false)
    private Hobbies hobby;

    @Column(name = "quantity")
    private int quantity = 1;
}
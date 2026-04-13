package org.example.hobbycatalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "hobbies")
public class Hobbies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hobby")
    private Long idHobby;

    @ManyToOne
    @JoinColumn(name = "id_type")
    private TypeHobbies typeAndHobbies;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "creator", columnDefinition = "VARCHAR(100)")
    private String creator;

    @Column(name = "price")
    private double price;

    // Связь с корзиной (один товар может быть в корзине у многих пользователей)
    @OneToMany(mappedBy = "hobby")
    @JsonIgnore
    private List<Cart> cartItems;
}
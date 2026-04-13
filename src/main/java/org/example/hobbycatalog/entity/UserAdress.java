package org.example.hobbycatalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_address")
public class UserAdress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Long id_adress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private int number_home;

    @Column(nullable = false)
    private int number_flat;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UsersInfo usersInfo_adress;
}
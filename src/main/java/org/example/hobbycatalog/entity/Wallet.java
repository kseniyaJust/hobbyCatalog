package org.example.hobbycatalog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Date;

@Entity
@Data
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wallet")
    private Long idWallet;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @JsonIgnore
    private UsersInfo usersInfo;

    @Column(name = "owner_name")
    private String owner_name;

    @Column(name = "cart_number")
    private Long cart_number;

    @Column(name = "date_expire")
    @JsonFormat(pattern = "MM/yyyy")
    private Date date_expire;

    @Column(name = "cvc")
    private Long CVC;
}
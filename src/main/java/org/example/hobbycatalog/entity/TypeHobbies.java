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
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.util.Set;

@Entity
@Data
@Table(name = "type_hobbies")
public class TypeHobbies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type")
    private Long idType;

    @Column(name = "type_name", unique = true)
    private String typeName;

    @Min(2)
    @Column(name = "count_players")
    private int countPlayers;

    @Column(name = "summary")
    private String summary;

    @OneToMany(mappedBy = "typeAndHobbies")
    @JsonIgnore
    private Set<Hobbies> hobbies_bound;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UsersInfo creatorUser;
}
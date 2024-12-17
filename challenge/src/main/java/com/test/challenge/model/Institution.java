package com.test.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "institution")
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long institutionId;
    @Column(length = 50, nullable = false, unique = true)
    private String name;
    @ToString.Exclude
    @JsonIgnore                                                //para que no entre en un bucle infinito
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<User> users;
}

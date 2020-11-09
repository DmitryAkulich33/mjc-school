package com.epam.esm.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString(of = {"id", "name", "surname", "lock"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name_user", nullable = false)
    private String name;

    @Column(name = "surname_user", nullable = false)
    private String surname;

    @Column(name = "lock_user")
    private Integer lock;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;

}

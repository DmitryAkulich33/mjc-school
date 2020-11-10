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
@NamedQueries({
        @NamedQuery(name = User.QueryNames.FIND_ALL,
                query = "SELECT u FROM user u WHERE lock_user=0"),
        @NamedQuery(name = User.QueryNames.FIND_BY_ID,
                query = "SELECT u FROM user u WHERE lock_user=0 AND id_user=:idUser")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name_user", nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(name = "lock_user")
    private Integer lock;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;

    public static final class QueryNames {
        public static final String FIND_BY_ID = "User.findById";
        public static final String FIND_ALL = "User.findAll";

        public QueryNames() {
        }
    }
}

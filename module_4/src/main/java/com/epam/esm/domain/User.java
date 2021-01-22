package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditUserListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@EntityListeners(AuditUserListener.class)
@ToString(exclude = "orders")
@EqualsAndHashCode(exclude = "orders")
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(name = "name_user", nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column
    private Boolean deleted;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;
}


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
}

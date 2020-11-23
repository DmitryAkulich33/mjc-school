package com.epam.esm.domain;

import com.epam.esm.dao.audit.AuditOrderListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EntityListeners(AuditOrderListener.class)
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order", updatable = false, nullable = false)
    private Long id;

    @Column(name = "purchase_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    @Column
    private Double total;

    @Column(name = "lock_order")
    private Integer lock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "certificate_order",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id_order")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id_certificate")})
    private List<Certificate> certificates;
}

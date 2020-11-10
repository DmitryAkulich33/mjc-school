package com.epam.esm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(of = {"id", "purchaseDate", "total", "user", "lock"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
@NamedQueries({
        @NamedQuery(name = Order.QueryNames.FIND_ALL,
                query = "SELECT o FROM orders o WHERE lock_order=0"),
        @NamedQuery(name = Order.QueryNames.FIND_BY_ID,
                query = "SELECT o FROM orders o WHERE lock_order=0 AND id_order=:idOrder"),
        @NamedQuery(name = Order.QueryNames.FIND_ALL_BY_USER_ID,
                query = "SELECT o FROM orders o WHERE lock_order=0 AND id_user=:idUser")
})
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "certificate_order",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id_order")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id_certificate")})
    private List<Certificate> certificates;

    public static final class QueryNames {
        public static final String FIND_BY_ID = "Order.getById";
        public static final String FIND_ALL = "Order.getAll";
        public static final String FIND_ALL_BY_USER_ID = "Order.getAllByUserId";

        public QueryNames() {
        }
    }
}

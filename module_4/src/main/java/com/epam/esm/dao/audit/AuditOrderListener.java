package com.epam.esm.dao.audit;

import com.epam.esm.domain.Order;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class AuditOrderListener {
    private static final Boolean DELETED = false;

    @PrePersist
    public void createOrder(Order order) {
        LocalDateTime purchaseDate = LocalDateTime.now();
        order.setPurchaseDate(purchaseDate);
        order.setDeleted(DELETED);
    }
}

package com.epam.esm.dao.audit;

import com.epam.esm.domain.Order;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class AuditOrderListener {
    private static final Integer LOCK = 0;

    @PrePersist
    public void createOrder(Order order) {
        LocalDateTime purchaseDate = LocalDateTime.now();
        order.setPurchaseDate(purchaseDate);
        order.setLock(LOCK);
    }
}

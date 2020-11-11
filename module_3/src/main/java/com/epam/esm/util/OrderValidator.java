package com.epam.esm.util;

import com.epam.esm.exceptions.OrderValidatorException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validateOrderId(Long id) {
        if (id == null || id <= 0) {
            throw new OrderValidatorException("message.invalid_order_id");
        }
    }
}

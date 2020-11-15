package com.epam.esm.util;

import com.epam.esm.exceptions.OrderValidatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderValidatorTest {
    private static final Long CORRECT_ORDER_ID = 1L;
    private static final Long WRONG_ORDER_ID = -1L;

    private OrderValidator orderValidator;

    @BeforeEach
    public void setUp() {
        orderValidator = new OrderValidator();
    }

    @Test
    public void testValidateOrderId() {
        orderValidator.validateOrderId(CORRECT_ORDER_ID);
    }

    @Test
    public void testValidateOrderId_OrderValidatorException() {
        assertThrows(OrderValidatorException.class, () -> {
            orderValidator.validateOrderId(WRONG_ORDER_ID);
        });
    }
}
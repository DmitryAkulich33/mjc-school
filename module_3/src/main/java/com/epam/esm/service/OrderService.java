package com.epam.esm.service;

import com.epam.esm.domain.Order;

import java.util.List;

public interface OrderService {
    Order getOrderById(Long idOrder);

    List<Order> getAllOrders();

    List<Order> getAllOrdersByUserId(Long idUser);
}

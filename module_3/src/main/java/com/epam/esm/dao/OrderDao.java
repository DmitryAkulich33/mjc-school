package com.epam.esm.dao;

import com.epam.esm.domain.Order;

import java.util.List;

public interface OrderDao {
    Order getOrderById(Long idOrder);

    List<Order> getAllOrders();

    List<Order> getAllOrdersByUserId(Long idUser);
}

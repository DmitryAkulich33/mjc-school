package com.epam.esm.dao;

import com.epam.esm.domain.Order;

import java.util.List;

public interface OrderDao {
    Order getOrderById(Long idOrder);

    List<Order> getOrders();

    List<Order> getOrdersByUserId(Long idUser);

    Order getDataByUserId(Long idUser, Long idOrder);
}

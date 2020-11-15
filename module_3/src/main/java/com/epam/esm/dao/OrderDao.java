package com.epam.esm.dao;

import com.epam.esm.domain.Order;

import java.util.List;

public interface OrderDao {
    Order getOrderById(Long idOrder);

    List<Order> getOrders(Integer offset, Integer pageSize);

    List<Order> getOrdersByUserId(Long idUser, Integer offset, Integer pageSize);

    Order getDataByUserId(Long idUser, Long idOrder);

    Order createOrder(Order order);
}

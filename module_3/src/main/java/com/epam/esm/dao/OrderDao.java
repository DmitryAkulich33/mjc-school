package com.epam.esm.dao;

import com.epam.esm.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Optional<Order> getOrderById(Long idOrder);

    List<Order> getOrders(Integer offset, Integer pageSize);

    List<Order> getOrdersByUserId(Long idUser, Integer offset, Integer pageSize);

    Optional<Order> getOrderDataByUserId(Long idUser, Long idOrder);

    Order createOrder(Order order);
}

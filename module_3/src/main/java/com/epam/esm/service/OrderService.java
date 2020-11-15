package com.epam.esm.service;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;

import java.util.List;

public interface OrderService {
    Order getOrderById(Long idOrder);

    List<Order> getOrders();

    List<Order> getOrdersByUserId(Long idUser);

    Order getDataByUserId(Long idUser, Long idOrder);

    Order makeOrder(Long idUser, List<Certificate> certificates);
}

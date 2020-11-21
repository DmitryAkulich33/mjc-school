package com.epam.esm.service;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;

import java.util.List;

public interface OrderService {
    Order getOrderById(Long idOrder);

    List<Order> getOrders(Integer pageNumber, Integer pageSize);

    List<Order> getOrdersByUserId(Long idUser, Integer pageNumber, Integer pageSize);

    Order getOrderDataByUserId(Long idUser, Long idOrder);

    Order makeOrder(Long idUser, List<Certificate> certificates);
}

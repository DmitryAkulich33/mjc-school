package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Order;
import com.epam.esm.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;

    private static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order getOrderById(Long idOrder) {
        log.debug(String.format("Service: search order by id %d", idOrder));
        return orderDao.getOrderById(idOrder);
    }

    @Override
    public List<Order> getAllOrders() {
        log.debug("Service: search all orders.");
        return orderDao.getAllOrders();
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long idUser) {
        log.debug("Service: search all users.");
        return orderDao.getAllOrdersByUserId(idUser);
    }
}

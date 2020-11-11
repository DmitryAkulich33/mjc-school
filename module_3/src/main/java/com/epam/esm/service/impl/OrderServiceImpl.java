package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.OrderValidator;
import com.epam.esm.util.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderValidator orderValidator;
    private final UserValidator userValidator;

    private static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, OrderValidator orderValidator, UserValidator userValidator) {
        this.orderDao = orderDao;
        this.orderValidator = orderValidator;
        this.userValidator = userValidator;
    }

    @Override
    public Order getOrderById(Long idOrder) {
        log.debug(String.format("Service: search order by id %d", idOrder));
        orderValidator.validateOrderId(idOrder);
        return orderDao.getOrderById(idOrder);
    }

    @Override
    public List<Order> getOrders() {
        log.debug("Service: search all orders.");
        return orderDao.getOrders();
    }

    @Override
    public List<Order> getOrdersByUserId(Long idUser) {
        log.debug("Service: search all users.");
        userValidator.validateUserId(idUser);
        return orderDao.getOrdersByUserId(idUser);
    }

    @Override
    public Order getDataByUserId(Long idUser, Long idOrder) {
        log.debug(String.format("Service: search order by id_order %d and id_user %d", idOrder, idUser));
        orderValidator.validateOrderId(idOrder);
        userValidator.validateUserId(idUser);
        return orderDao.getDataByUserId(idUser, idOrder);
    }
}

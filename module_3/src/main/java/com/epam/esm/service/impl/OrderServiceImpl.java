package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.OrderValidator;
import com.epam.esm.util.PaginationValidator;
import com.epam.esm.util.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderValidator orderValidator;
    private final UserValidator userValidator;
    private final PaginationValidator paginationValidator;
    private final UserService userService;
    private final CertificateService certificateService;

    private static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, OrderValidator orderValidator, UserValidator userValidator, PaginationValidator paginationValidator, UserService userService, CertificateService certificateService) {
        this.orderDao = orderDao;
        this.orderValidator = orderValidator;
        this.userValidator = userValidator;
        this.paginationValidator = paginationValidator;
        this.userService = userService;
        this.certificateService = certificateService;
    }

    @Override
    public Order getOrderById(Long idOrder) {
        log.debug(String.format("Service: search order by id %d", idOrder));
        orderValidator.validateOrderId(idOrder);
        Optional<Order> optionalOrder = orderDao.getOrderById(idOrder);
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException("message.wrong_order_id"));
    }

    @Override
    public List<Order> getOrders(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all orders.");
        paginationValidator.validatePagination(pageNumber, pageSize);
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return orderDao.getOrders(offset, pageSize);
        } else {
            return orderDao.getOrders();
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long idUser, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        userValidator.validateUserId(idUser);
        paginationValidator.validatePagination(pageNumber, pageSize);
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return orderDao.getOrdersByUserId(idUser, offset, pageSize);
        } else {
            return orderDao.getOrdersByUserId(idUser);
        }
    }

    @Override
    public Order getOrderDataByUserId(Long idUser, Long idOrder) {
        log.debug(String.format("Service: search order by id_order %d and id_user %d", idOrder, idUser));
        orderValidator.validateOrderId(idOrder);
        userValidator.validateUserId(idUser);
        userService.getUserById(idUser);
        Optional<Order> optionalOrder = orderDao.getOrderDataByUserId(idUser, idOrder);
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException("message.wrong_order_id"));
    }

    @Override
    public Order makeOrder(Long idUser, List<Certificate> certificatesFromQuery) {
        log.debug("Service: make order.");
        userValidator.validateUserId(idUser);
        List<Certificate> certificates = getCertificates(certificatesFromQuery);
        double total = getTotal(certificates);
        Order order = new Order();
        User user = userService.getUserById(idUser);
        order.setCertificates(certificates);
        order.setTotal(total);
        order.setUser(user);
        return orderDao.createOrder(order);
    }

    private List<Certificate> getCertificates(List<Certificate> certificatesFromQuery) {
        return certificatesFromQuery.stream()
                .map(certificate -> certificateService.getCertificateById(certificate.getId()))
                .collect(Collectors.toList());
    }

    private double getTotal(List<Certificate> certificates) {
        return certificates.stream().mapToDouble(Certificate::getPrice).sum();
    }
}

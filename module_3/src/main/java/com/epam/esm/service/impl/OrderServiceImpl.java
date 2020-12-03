package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
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
    private final UserService userService;
    private final CertificateService certificateService;

    private static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserService userService, CertificateService certificateService) {
        this.orderDao = orderDao;
        this.userService = userService;
        this.certificateService = certificateService;
    }

    @Override
    public Order getOrderById(Long idOrder) {
        log.debug(String.format("Service: search order by id %d", idOrder));
        Optional<Order> optionalOrder = orderDao.getOrderById(idOrder);
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException("message.wrong_order_id"));
    }

    @Override
    public List<Order> getOrders(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all orders.");
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return orderDao.getOrders(offset, pageSize);
        } else if (pageNumber == null && pageSize == null) {
            return orderDao.getOrders();
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long idUser, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        if (pageNumber != null && pageSize != null) {
            Integer offset = calculateOffset(pageNumber, pageSize);
            return orderDao.getOrdersByUserId(idUser, offset, pageSize);
        } else if (pageNumber == null && pageSize == null) {
            return orderDao.getOrdersByUserId(idUser);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Override
    public Order getOrderDataByUserId(Long idUser, Long idOrder) {
        log.debug(String.format("Service: search order by id_order %d and id_user %d", idOrder, idUser));
        userService.getUserById(idUser);
        Optional<Order> optionalOrder = orderDao.getOrderDataByUserId(idUser, idOrder);
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException("message.wrong_order_id"));
    }

    @Override
    public Order makeOrder(Long idUser, List<Certificate> certificatesFromQuery) {
        log.debug("Service: make order.");
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

    double getTotal(List<Certificate> certificates) {
        return certificates.stream().mapToDouble(Certificate::getPrice).sum();
    }
}

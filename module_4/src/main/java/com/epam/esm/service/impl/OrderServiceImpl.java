package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final CertificateService certificateService;
    private final OrderRepository orderRepository;

    private static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(UserService userService, CertificateService certificateService, OrderRepository orderRepository) {
        this.userService = userService;
        this.certificateService = certificateService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOrderById(Long idOrder) {
        log.debug(String.format("Service: search order by id %d", idOrder));
        Optional<Order> optionalOrder = orderRepository.getEntityById(false, idOrder);
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException("message.wrong_order_id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> getOrders(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all orders.");
        if (pageNumber != null && pageSize != null) {
            checkPaginationGetOrders(pageNumber, pageSize);
            return orderRepository.getEntities(false, PageRequest.of(pageNumber - 1, pageSize));
        } else if (pageNumber == null && pageSize == null) {
            return orderRepository.getEntities(false);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    private void checkPaginationGetOrders(Integer pageNumber, Integer pageSize) {
        long countFromDb = orderRepository.count();
        long countFromRequest = (pageNumber - 1) * pageSize;
        if (countFromDb <= countFromRequest) {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> getOrdersByUserId(Long idUser, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        if (pageNumber != null && pageSize != null) {
            checkPaginationGetOrdersByUserId(idUser, pageNumber, pageSize);
            return orderRepository.findByUserId(idUser, PageRequest.of(pageNumber - 1, pageSize));
        } else if (pageNumber == null && pageSize == null) {
            return orderRepository.findByUserId(idUser);
        } else {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    private void checkPaginationGetOrdersByUserId(Long idUser, Integer pageNumber, Integer pageSize) {
        long countFromDb = orderRepository.countAllByUserId(idUser);
        long countFromRequest = (pageNumber - 1) * pageSize;
        if (countFromDb <= countFromRequest) {
            throw new WrongEnteredDataException("message.invalid_entered_data");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Order getOrderDataByUserId(Long idUser, Long idOrder) {
        log.debug(String.format("Service: search order by id_order %d and id_user %d", idOrder, idUser));
        userService.getUserById(idUser);
        Optional<Order> optionalOrder = orderRepository.findByUserIdAndId(idUser, idOrder);
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException("message.wrong_order_id"));
    }

    @Transactional
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
        return orderRepository.save(order);
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

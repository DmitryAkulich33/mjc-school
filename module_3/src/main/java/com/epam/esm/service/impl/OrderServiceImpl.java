package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.util.OrderValidator;
import com.epam.esm.util.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    /**
     * Dao for this server
     */
    private final OrderDao orderDao;

    /**
     * Dao for this server
     */
    private final UserDao userDao;

    /**
     * Dao for this server
     */
    private final CertificateDao certificateDao;

    /**
     * Validator for this service
     */
    private final OrderValidator orderValidator;

    /**
     * Validator for this service
     */
    private final UserValidator userValidator;

    /**
     * Offset's calculator for this service
     */
    private final OffsetCalculator offsetCalculator;

    /**
     * Logger for this service
     */
    private static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    /**
     * Constructor - creating a new object
     *
     * @param orderDao         dao for this server
     * @param userDao          dao for this server
     * @param certificateDao   dao for this server
     * @param orderValidator   validator for this service
     * @param userValidator    Validator validator for this service
     * @param offsetCalculator offset's calculator for this service
     */
    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, CertificateDao certificateDao, OrderValidator orderValidator, UserValidator userValidator, OffsetCalculator offsetCalculator) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.certificateDao = certificateDao;
        this.orderValidator = orderValidator;
        this.userValidator = userValidator;
        this.offsetCalculator = offsetCalculator;
    }

    /**
     * Get order by id
     *
     * @param idOrder tag's id
     * @return order
     */
    @Override
    public Order getOrderById(Long idOrder) {
        log.debug(String.format("Service: search order by id %d", idOrder));
        orderValidator.validateOrderId(idOrder);
        return orderDao.getOrderById(idOrder);
    }

    /**
     * Get all orders
     *
     * @param pageNumber page number
     * @param pageSize   page size
     * @return list of orders
     */
    @Override
    public List<Order> getOrders(Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all orders.");
        Integer offset = offsetCalculator.calculateOffset(pageNumber, pageSize);
        return orderDao.getOrders(offset, pageSize);
    }

    /**
     * Get orders by user id
     *
     * @param idUser     user's id
     * @param pageNumber page number
     * @param pageSize   page size
     * @return list of user's orders
     */
    @Override
    public List<Order> getOrdersByUserId(Long idUser, Integer pageNumber, Integer pageSize) {
        log.debug("Service: search all users.");
        userValidator.validateUserId(idUser);
        Integer offset = offsetCalculator.calculateOffset(pageNumber, pageSize);
        return orderDao.getOrdersByUserId(idUser, offset, pageSize);
    }

    /**
     * Get information about order's total and purchase date
     *
     * @param idUser  user's id
     * @param idOrder order's id
     * @return list of user's orders
     */
    @Override
    public Order getDataByUserId(Long idUser, Long idOrder) {
        log.debug(String.format("Service: search order by id_order %d and id_user %d", idOrder, idUser));
        orderValidator.validateOrderId(idOrder);
        userValidator.validateUserId(idUser);
        return orderDao.getDataByUserId(idUser, idOrder);
    }

    /**
     * Create order
     *
     * @param idUser                user's id
     * @param certificatesFromQuery list of certificates from query
     * @return tag
     */
    @Override
    public Order makeOrder(Long idUser, List<Certificate> certificatesFromQuery) {
        List<Certificate> certificates = getCertificates(certificatesFromQuery);
        double total = getTotal(certificates);
        Order order = new Order();
        User user = userDao.getUserById(idUser);
        order.setCertificates(certificates);
        order.setTotal(total);
        order.setUser(user);
        return orderDao.createOrder(order);
    }

    private List<Certificate> getCertificates(List<Certificate> certificatesFromQuery) {
        return certificatesFromQuery.stream()
                .map(certificate -> certificateDao.getCertificateById(certificate.getId()))
                .collect(Collectors.toList());
    }

    private double getTotal(List<Certificate> certificates) {
        return certificates.stream().mapToDouble(Certificate::getPrice).sum();
    }
}

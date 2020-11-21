package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.OrderDaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderDaoImplTest {
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final String CERTIFICATE_NAME_1 = "Certificate for one purchase";
    private static final String CERTIFICATE_NAME_2 = "Certificate for dinner in a restaurant";
    private static final String CERTIFICATE_DESCRIPTION_1 = "Certificate for one going to the shop";
    private static final String CERTIFICATE_DESCRIPTION_2 = "Food and drink without check limit at Viet Express";
    private static final Double CERTIFICATE_PRICE_1 = 50.0;
    private static final Double CERTIFICATE_PRICE_2 = 100.0;
    private static final Integer CERTIFICATE_DURATION_1 = 365;
    private static final Integer CERTIFICATE_DURATION_2 = 100;
    private static final String USER_NAME_1 = "Ivan";
    private static final String USER_NAME_2 = "Petr";
    private static final String USER_SURNAME_1 = "Ivanov";
    private static final String USER_SURNAME_2 = "Petrov";
    private static final String ORDER_DATE_1 = "2020-11-22T10:45:11";
    private static final String ORDER_DATE_2 = "2020-11-22T12:45:11";
    private static final Double ORDER_TOTAL_1 = 150.0;
    private static final Double ORDER_TOTAL_2 = 50.0;
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_10 = 10;
    private static final Integer OFFSET_0 = 0;
    private static final Integer OFFSET_2 = 2;
    private static final Integer WRONG_OFFSET = -2;

    private Tag tag1;
    private Tag tag2;
    private Tag tag3;

    private Certificate certificate1;
    private Certificate certificate2;

    private User user1;
    private User user2;

    private Order order1;
    private Order order2;
    private Order createOrder;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        tag3 = new Tag();
        tag3.setName(TAG_NAME_1);

        List<Tag> tags1 = new ArrayList<>(Arrays.asList(tag1, tag2));
        List<Tag> tags2 = new ArrayList<>(Collections.singletonList(tag1));

        certificate1 = new Certificate();
        certificate1.setName(CERTIFICATE_NAME_1);
        certificate1.setDescription(CERTIFICATE_DESCRIPTION_1);
        certificate1.setPrice(CERTIFICATE_PRICE_1);
        certificate1.setDuration(CERTIFICATE_DURATION_1);
        certificate1.setTags(tags1);

        certificate2 = new Certificate();
        certificate2.setName(CERTIFICATE_NAME_2);
        certificate2.setDescription(CERTIFICATE_DESCRIPTION_2);
        certificate2.setPrice(CERTIFICATE_PRICE_2);
        certificate2.setDuration(CERTIFICATE_DURATION_2);
        certificate2.setTags(tags2);

        List<Certificate> certificates1 = new ArrayList<>(Arrays.asList(certificate1, certificate2));
        List<Certificate> certificates2 = new ArrayList<>(Collections.singletonList(certificate1));

        user1 = new User();
        user1.setName(USER_NAME_1);
        user1.setSurname(USER_SURNAME_1);
        user1.setLock(LOCK);

        user2 = new User();
        user2.setName(USER_NAME_2);
        user2.setSurname(USER_SURNAME_2);
        user2.setLock(LOCK);

        order1 = new Order();
        order1.setTotal(ORDER_TOTAL_1);
        order1.setUser(user1);
        order1.setCertificates(certificates1);

        order2 = new Order();
        order2.setTotal(ORDER_TOTAL_2);
        order2.setUser(user2);
        order2.setCertificates(certificates2);

        entityManager.persist(tag1);
        entityManager.persist(tag2);
        entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        entityManager.persist(user1);
        entityManager.persist(user2);

        createOrder = new Order();
        createOrder.setTotal(ORDER_TOTAL_1);
        createOrder.setUser(user1);
        createOrder.setCertificates(certificates1);
    }

    @Autowired
    private OrderDao orderDao;

    @Test
    public void testGetOrderById() {
        Order expected = entityManager.persist(order1);

        Order actual = orderDao.getOrderById(expected.getId()).get();

        assertEquals(expected, actual);
        assert actual.getId() > 0;
    }

    @Test
    public void testGetOrderById_NotFound() {
        Optional<Order> expected = Optional.empty();

        Optional<Order> actual = orderDao.getOrderById(ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTags() {
        Order expected1 = entityManager.persist(order1);
        Order expected2 = entityManager.persist(order2);
        List<Order> expected = new ArrayList<>(Arrays.asList(expected1, expected2));

        List<Order> actual = orderDao.getOrders(OFFSET_0, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
        assert !actual.isEmpty();
    }

    @Test
    public void testGetOrders_Pagination() {
        Order expected1 = entityManager.persist(order1);
        Order expected2 = entityManager.persist(order2);
        List<Order> expected = new ArrayList<>(Collections.singletonList(expected1));

        List<Order> actual = orderDao.getOrders(OFFSET_0, PAGE_SIZE_1);

        Assertions.assertEquals(expected, actual);
        assert !actual.isEmpty();
        assert !actual.contains(expected2);
        assert actual.size() == PAGE_SIZE_1;
    }

    @Test
    public void testGetOrders_Pagination_NotFound() {
        entityManager.persist(order1);
        entityManager.persist(order2);

        List<Order> actual = orderDao.getOrders(OFFSET_2, PAGE_SIZE_10);

        assert actual.isEmpty();
    }

    @Test
    public void testGetOrders_OrderDaoException() {
        assertThrows(OrderDaoException.class, () -> {
            orderDao.getOrders(WRONG_OFFSET, PAGE_SIZE_10);
        });
    }

    @Test
    public void testGetOrderByUserId() {
        Order order = entityManager.persist(order1);
        List<Order> expected = new ArrayList<>(Collections.singletonList(order));

        List<Order> actual = orderDao.getOrdersByUserId(order.getUser().getId(), OFFSET_0, PAGE_SIZE_10);

        assertEquals(expected, actual);
        assert actual.get(0).getId().equals(expected.get(0).getId());
        assert actual.get(0).getUser().equals(expected.get(0).getUser());
    }

    @Test
    public void testGetOrderByUserId_NotFound() {
        Order order = entityManager.persist(order1);
        List<Order> expected = new ArrayList<>();

        List<Order> actual = orderDao.getOrdersByUserId(ID_2, OFFSET_0, PAGE_SIZE_10);

        assertEquals(expected, actual);
        assert !actual.contains(order);
    }

    @Test
    public void testGetOrderDataByUserId() {
        Order expected = entityManager.persist(order1);
        Long idUser = expected.getUser().getId();
        Long idOrder = expected.getId();

        Order actual = orderDao.getOrderDataByUserId(idUser, idOrder).get();

        assertEquals(expected, actual);
        assert actual.getUser().getId().equals(idUser);
        assert actual.getId().equals(idOrder);
        assert actual.getPurchaseDate().equals(expected.getPurchaseDate());
        assert actual.getTotal().equals(expected.getTotal());
    }

    @Test
    public void testGetOrderDataByUserId_NotFound() {
        Order order = entityManager.persist(order1);
        Long idOrder = order.getId();
        Optional<Order> expected = Optional.empty();

        Optional<Order> actual = orderDao.getOrderDataByUserId(ID_2, idOrder);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateCertificate() {
        LocalDateTime currentDate = LocalDateTime.now().minusSeconds(2);

        Order actual = orderDao.createOrder(createOrder);

        assert actual.getId() > 0;
        assert actual.getPurchaseDate().isAfter(currentDate);
        assert actual.getTotal().equals(createOrder.getTotal());
        assert actual.getLock().equals(LOCK);
        assert actual.getUser().equals(createOrder.getUser());
        assert actual.getCertificates().equals(createOrder.getCertificates());
    }

    @Test
    public void testMakeOrder_OrderDaoException() {
        createOrder.setId(ID_1);
        assertThrows(OrderDaoException.class, () -> {
            orderDao.createOrder(createOrder);
        });
    }
}
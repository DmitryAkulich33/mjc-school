package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.*;
import com.epam.esm.domain.Order;
import com.epam.esm.exceptions.OrderDaoException;
import com.epam.esm.exceptions.WrongEnteredDataException;
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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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
    private static final Double ORDER_TOTAL_1 = 150.0;
    private static final Double ORDER_TOTAL_2 = 50.0;
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_2 = 2;
    private static final Integer OFFSET_0 = 0;
    private static final Integer OFFSET_2 = 2;
    private static final Integer WRONG_OFFSET = -2;

    private Order order1;
    private Order order2;
    private Order createOrder;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);

        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

        Tag tag3 = new Tag();
        tag3.setName(TAG_NAME_1);

        List<Tag> tags1 = Arrays.asList(tag1, tag2);
        List<Tag> tags2 = Collections.singletonList(tag1);

        Certificate certificate1 = new Certificate();
        certificate1.setName(CERTIFICATE_NAME_1);
        certificate1.setDescription(CERTIFICATE_DESCRIPTION_1);
        certificate1.setPrice(CERTIFICATE_PRICE_1);
        certificate1.setDuration(CERTIFICATE_DURATION_1);
        certificate1.setTags(tags1);

        Certificate certificate2 = new Certificate();
        certificate2.setName(CERTIFICATE_NAME_2);
        certificate2.setDescription(CERTIFICATE_DESCRIPTION_2);
        certificate2.setPrice(CERTIFICATE_PRICE_2);
        certificate2.setDuration(CERTIFICATE_DURATION_2);
        certificate2.setTags(tags2);

        List<Certificate> certificates1 = Arrays.asList(certificate1, certificate2);
        List<Certificate> certificates2 = Collections.singletonList(certificate1);

        User user1 = new User();
        user1.setName(USER_NAME_1);
        user1.setSurname(USER_SURNAME_1);
        user1.setLock(LOCK);

        User user2 = new User();
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
    }

    @Test
    public void testGetOrderById_NotFound() {
        Optional<Order> expected = Optional.empty();

        Optional<Order> actual = orderDao.getOrderById(ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrders() {
        Order expected1 = entityManager.persist(order1);
        Order expected2 = entityManager.persist(order2);
        List<Order> expected = Arrays.asList(expected1, expected2);

        List<Order> actual = orderDao.getOrders();

        Assertions.assertEquals(expected, actual);
        assert !actual.isEmpty();
    }

    @Test
    public void testGetOrders_Pagination() {
        Order expected1 = entityManager.persist(order1);
        Order expected2 = entityManager.persist(order2);
        List<Order> expected = Collections.singletonList(expected1);

        List<Order> actual = orderDao.getOrders(OFFSET_0, PAGE_SIZE_1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetOrders_Pagination_WrongEnteredDataException() {
        entityManager.persist(order1);
        entityManager.persist(order2);

        assertThrows(WrongEnteredDataException.class, () -> {
            orderDao.getOrders(OFFSET_2, PAGE_SIZE_2);
        });
    }

    @Test
    public void testGetOrders_OrderDaoException() {
        assertThrows(OrderDaoException.class, () -> {
            orderDao.getOrders(WRONG_OFFSET, PAGE_SIZE_2);
        });
    }

    @Test
    public void testGetOrderByUserId() {
        Order order = entityManager.persist(order1);
        List<Order> expected = Collections.singletonList(order);

        List<Order> actual = orderDao.getOrdersByUserId(order.getUser().getId(), OFFSET_0, PAGE_SIZE_2);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderByUserId_NotFound() {
        entityManager.persist(order1);
        List<Order> expected = new ArrayList<>();

        List<Order> actual = orderDao.getOrdersByUserId(ID_2, OFFSET_0, PAGE_SIZE_2);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderDataByUserId() {
        Order expected = entityManager.persist(order1);
        Long idUser = expected.getUser().getId();
        Long idOrder = expected.getId();

        Order actual = orderDao.getOrderDataByUserId(idUser, idOrder).get();

        assertEquals(expected, actual);
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

//    @Test
//    public void test() {
//        Order order3 = new Order();
//        order3.setTotal(120.0);
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setName(USER_NAME_2);
//        user2.setSurname(USER_SURNAME_2);
//        user2.setLock(LOCK);
//        order3.setUser(user2);
//        Certificate certificate1 = new Certificate();
//        certificate1.setName(CERTIFICATE_NAME_1);
//        certificate1.setDescription(CERTIFICATE_DESCRIPTION_1);
//        certificate1.setPrice(CERTIFICATE_PRICE_1);
//        certificate1.setDuration(CERTIFICATE_DURATION_1);
//        Tag tag1 = new Tag();
//        tag1.setName(TAG_NAME_1);
//
//        Tag tag2 = new Tag();
//        tag2.setName(TAG_NAME_2);
//
//        List<Tag> tags1 = Arrays.asList(tag1, tag2);
//        certificate1.setTags(tags1);
//        List<Certificate> certificates2 = Collections.singletonList(certificate1);
//        order2.setCertificates(certificates2);
//
//        Order expected1 = entityManager.persist(order1);
//        Order expected2 = entityManager.persist(order2);
//        Order expected3 = entityManager.persist(order3);
//
//        System.out.println(expected1.getTotal());
//        System.out.println(expected2.getTotal());
//        System.out.println(expected3.getTotal());
//
//        Query query = entityManager.getEntityManager().createQuery(
////                "select o.user from orders o join o.user u group by o.user order by sum(o.total) desc"
//                "select t from tag t join o.user from orders o join o.user u group by o.user order by sum(o.total) desc"
//        );
//
//        List<Tag> actual = query.getResultList();
//        System.out.println("-------" + actual);
//    }
}
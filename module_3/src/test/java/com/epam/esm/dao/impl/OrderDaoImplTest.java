package com.epam.esm.dao.impl;

import com.epam.esm.config.DbConfig;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {DbConfig.class})
@Transactional
@SqlGroup({
        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
class OrderDaoImplTest {
    private static final Long CORRECT_ID_1 = 1L;
    private static final Long CORRECT_ID_2 = 2L;
    private static final Long CORRECT_ID_3 = 3L;
    private static final Long WRONG_ID = 100L;
    private static final String CERTIFICATE_NAME_1 = "Certificate for one purchase";
    private static final String CERTIFICATE_NAME_2 = "Certificate for dinner in a restaurant";
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_10 = 10;
    private static final Integer OFFSET = 0;
    private static final Integer WRONG_OFFSET = -2;
    private static final String TAG_NAME_1 = "food";
    private static final String TAG_NAME_2 = "delivery";
    private static final String TAG_NAME_3 = "spa";
    private static final String CREATION_DATE_2 = "2020-11-22T12:45:11";

    private Order order1;
    private Order order2;
    private Order order3;
    private Order createOrder;

    private User user1;
    private User user2;

    private Certificate certificate1;
    private Certificate certificate2;

    private Tag tag1;
    private Tag tag2;
    private Tag newTag;

    private List<Tag> tags1;
    private List<Tag> tags2;

    private List<Certificate> certificates1;
    private List<Certificate> certificates2;

    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    public void setUp() {
        tag1 = new Tag();
        tag1.setId(CORRECT_ID_1);
        tag1.setName(TAG_NAME_1);
        tag1.setLock(LOCK);

        tag2 = new Tag();
        tag2.setId(CORRECT_ID_2);
        tag2.setName(TAG_NAME_2);
        tag2.setLock(LOCK);

        newTag = new Tag();
        newTag.setName(TAG_NAME_3);

        tags1 = new ArrayList<>(Arrays.asList(tag1, tag2));
        tags2 = new ArrayList<>(Collections.singletonList(tag1));

        certificate1 = new Certificate();
        certificate1.setId(CORRECT_ID_1);
        certificate1.setName(CERTIFICATE_NAME_1);
        certificate1.setDescription("Certificate for one going to the shop");
        certificate1.setPrice(50.0);
        certificate1.setCreateDate(LocalDateTime.parse("2020-10-22T11:45:11"));
        certificate1.setLock(LOCK);
        certificate1.setDuration(365);
        certificate1.setTags(tags1);

        certificate2 = new Certificate();
        certificate2.setId(CORRECT_ID_2);
        certificate2.setName(CERTIFICATE_NAME_2);
        certificate2.setDescription("Food and drink without check limit at Viet Express");
        certificate2.setPrice(100.0);
        certificate2.setCreateDate(LocalDateTime.parse(CREATION_DATE_2));
        certificate2.setLock(LOCK);
        certificate2.setDuration(100);
        certificate2.setTags(tags2);

        certificates1 = new ArrayList<>(Arrays.asList(certificate1, certificate2));
        certificates2 = new ArrayList<>(Collections.singletonList(certificate1));

        user1 = new User();
        user1.setId(CORRECT_ID_1);
        user1.setName("Ivan");
        user1.setSurname("Ivanov");
        user1.setLock(LOCK);

        user2 = new User();
        user2.setId(CORRECT_ID_2);
        user2.setName("Petr");
        user2.setSurname("Petrov");
        user2.setLock(LOCK);

        order1 = new Order();
        order1.setId(CORRECT_ID_1);
        order1.setPurchaseDate(LocalDateTime.parse("2020-11-22T12:45:11"));
        order1.setTotal(150.0);
        order1.setUser(user1);
        order1.setLock(LOCK);
        order1.setCertificates(certificates1);

        order2 = new Order();
        order2.setId(CORRECT_ID_2);
        order2.setPurchaseDate(LocalDateTime.parse("2020-11-22T12:45:11"));
        order2.setTotal(50.0);
        order2.setUser(user2);
        order2.setLock(LOCK);
        order2.setCertificates(certificates2);

        order3 = new Order();
        order3.setTotal(100.0);
        order3.setUser(user2);
        order3.setCertificates(new ArrayList<>(Collections.singletonList(certificate2)));

        createOrder = new Order();
        createOrder.setId(CORRECT_ID_3);
        createOrder.setPurchaseDate(LocalDateTime.parse(CREATION_DATE_2));
        createOrder.setTotal(100.0);
        createOrder.setUser(user2);
        createOrder.setLock(LOCK);
        createOrder.setCertificates(new ArrayList<>(Collections.singletonList(certificate2)));
    }

//    @Test
//    public void testGetOrderById() {
//        Optional<Order> expected = Optional.ofNullable(order1);
//
//        Optional<Order> actual = orderDao.getOrderById(CORRECT_ID_1);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrderById_WrongResult() {
//        Optional<Order> expected = Optional.ofNullable(order1);
//
//        Optional<Order> actual = orderDao.getOrderById(CORRECT_ID_2);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrderById_NotFound() {
//        Optional<Order> expected = Optional.empty();
//
//        Optional<Order> actual = orderDao.getOrderById(WRONG_ID);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrders() {
//        List<Order> expected = new ArrayList<>(Arrays.asList(order1, order2));
//
//        List<Order> actual = orderDao.getOrders(OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrders_OrderDaoException() {
//        assertThrows(OrderDaoException.class, () -> {
//            orderDao.getOrders(WRONG_OFFSET, PAGE_SIZE_10);
//        });
//    }
//
//    @Test
//    public void testGetOrders_Pagination() {
//        List<Order> expected = new ArrayList<>(Collections.singletonList(order1));
//
//        List<Order> actual = orderDao.getOrders(OFFSET, PAGE_SIZE_1);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrders_WrongResult() {
//        List<Order> expected = new ArrayList<>(Collections.singletonList(order1));
//
//        List<Order> actual = orderDao.getOrders(OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrdersByUserId() {
//        List<Order> expected = new ArrayList<>(Collections.singletonList(order1));
//
//        List<Order> actual = orderDao.getOrdersByUserId(CORRECT_ID_1, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrdersByUserId_WrongResult() {
//        List<Order> expected = new ArrayList<>(Collections.singletonList(order1));
//
//        List<Order> actual = orderDao.getOrdersByUserId(CORRECT_ID_2, OFFSET, PAGE_SIZE_10);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetDataByUserId() {
//        Optional<Order> expected = Optional.ofNullable(order1);
//
//        Optional<Order> actual = orderDao.getDataByUserId(CORRECT_ID_1, CORRECT_ID_1);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetDataByUserId_WrongResult() {
//        Optional<Order> expected = Optional.ofNullable(order2);
//
//        Optional<Order> actual = orderDao.getDataByUserId(CORRECT_ID_1, CORRECT_ID_1);
//
//        Assertions.assertNotEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetDataByUserId_NotFound() {
//        Optional<Order> expected = Optional.empty();
//
//        Optional<Order> actual = orderDao.getDataByUserId(CORRECT_ID_1, CORRECT_ID_2);
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testMakeOrder() {
//        Order actual = orderDao.createOrder(order3);
//        actual.setPurchaseDate(LocalDateTime.parse(CREATION_DATE_2));
//
//        Assertions.assertEquals(createOrder, actual);
//    }
//
//    @Test
//    public void testMakeOrder_GetOrderById() {
//        orderDao.createOrder(order3);
//        Order actual = orderDao.getOrderById(CORRECT_ID_3).get();
//        actual.setPurchaseDate(LocalDateTime.parse(CREATION_DATE_2));
//
//        Assertions.assertEquals(createOrder, actual);
//    }
//
//    @Test
//    public void testMakeOrder_GetOrderById_WrongResult() {
//        orderDao.createOrder(order3);
//        Order actual = orderDao.getOrderById(CORRECT_ID_2).get();
//        actual.setPurchaseDate(LocalDateTime.parse(CREATION_DATE_2));
//
//        Assertions.assertNotEquals(createOrder, actual);
//    }
}
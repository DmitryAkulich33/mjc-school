package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderRepository;
import com.epam.esm.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderRepositoryTest {
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
    private static final String USER_LOGIN_1 = "user1";
    private static final String USER_LOGIN_2 = "user2";
    private static final String USER_PASSWORD_1 = "user1";
    private static final String USER_PASSWORD_2 = "user2";
    private static final String ROLE_NAME = "ROLE_USER";
    private static final Double ORDER_TOTAL_1 = 150.0;
    private static final Double ORDER_TOTAL_2 = 50.0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_2 = 2;
    private static final Integer PAGE_NUMBER_1 = 0;

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

        Role role = new Role();
        role.setName(ROLE_NAME);

        List<Role> roles = new ArrayList<>(Collections.singletonList(role));

        User user1 = new User();
        user1.setName(USER_NAME_1);
        user1.setSurname(USER_SURNAME_1);
        user1.setLogin(USER_LOGIN_1);
        user1.setPassword(USER_PASSWORD_1);
        user1.setRoles(roles);

        User user2 = new User();
        user2.setName(USER_NAME_2);
        user2.setSurname(USER_SURNAME_2);
        user2.setLogin(USER_LOGIN_2);
        user2.setPassword(USER_PASSWORD_2);
        user2.setRoles(roles);

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
        entityManager.persist(role);
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
    private OrderRepository orderRepository;

    @Test
    public void testGetOrderById() {
        Order expected = entityManager.persist(order1);

        Order actual = orderRepository.getEntityById(false, expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderById_NotFound() {
        Optional<Order> expected = Optional.empty();

        Optional<Order> actual = orderRepository.getEntityById(false, ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrders() {
        Order expected1 = entityManager.persist(order1);
        Order expected2 = entityManager.persist(order2);
        List<Order> expected = Arrays.asList(expected1, expected2);

        List<Order> actual = orderRepository.getEntities(false);

        Assertions.assertEquals(expected, actual);
        assert !actual.isEmpty();
    }

    @Test
    public void testGetOrders_Pagination() {
        Order expected1 = entityManager.persist(order1);
        Order expected2 = entityManager.persist(order2);
        List<Order> expected = Collections.singletonList(expected1);

        List<Order> actual = orderRepository.getEntities(false, PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_1));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderByUserId() {
        Order order = entityManager.persist(order1);
        List<Order> expected = Collections.singletonList(order);

        List<Order> actual = orderRepository.findByUserId(order.getUser().getId(), PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_2));

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderByUserId_NotFound() {
        entityManager.persist(order1);
        List<Order> expected = new ArrayList<>();

        List<Order> actual = orderRepository.findByUserId(ID_2, PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_2));

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderDataByUserId() {
        Order expected = entityManager.persist(order1);
        Long idUser = expected.getUser().getId();
        Long idOrder = expected.getId();

        Order actual = orderRepository.findByUserIdAndId(idUser, idOrder).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderDataByUserId_NotFound() {
        Order order = entityManager.persist(order1);
        Long idOrder = order.getId();
        Optional<Order> expected = Optional.empty();

        Optional<Order> actual = orderRepository.findByUserIdAndId(ID_2, idOrder);

        assertEquals(expected, actual);
    }

    @Test
    public void testMakeOrder() {
        LocalDateTime currentDate = LocalDateTime.now().minusSeconds(2);

        Order actual = orderRepository.save(createOrder);

        assert actual.getId() > 0;
        assert actual.getPurchaseDate().isAfter(currentDate);
        assert actual.getTotal().equals(createOrder.getTotal());
        assert actual.getDeleted().equals(false);
        assert actual.getUser().equals(createOrder.getUser());
        assert actual.getCertificates().equals(createOrder.getCertificates());
    }
}
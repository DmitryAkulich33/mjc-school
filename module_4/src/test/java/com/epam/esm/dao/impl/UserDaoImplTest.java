package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.domain"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoImplTest {
    private static final Long ID_1 = 1L;
    private static final String NAME_1 = "Ivan";
    private static final String NAME_2 = "Petr";
    private static final String SURNAME_1 = "Ivanov";
    private static final String SURNAME_2 = "Petrov";
    private static final String LOGIN_1 = "user1";
    private static final String LOGIN_2 = "user2";
    private static final String PASSWORD_1 = "user1";
    private static final String PASSWORD_2 = "user2";
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_NUMBER_1 = 0;
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
    private static final Double ORDER_TOTAL_1 = 150.0;
    private static final Double ORDER_TOTAL_2 = 50.0;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.setName(NAME_1);
        user1.setSurname(SURNAME_1);
        user1.setLogin(LOGIN_1);
        user1.setPassword(PASSWORD_1);

        user2 = new User();
        user2.setName(NAME_2);
        user2.setSurname(SURNAME_2);
        user2.setLogin(LOGIN_2);
        user2.setPassword(PASSWORD_2);
    }

    @Test
    public void testGetUserById() {
        User expected = entityManager.persist(user1);

        User actual = userRepository.getEntityById(false, expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUserById_NotFound() {
        Optional<User> expected = Optional.empty();

        Optional<User> actual = userRepository.getEntityById(false, ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUserByLogin() {
        User expected = entityManager.persist(user1);

        User actual = userRepository.findByLogin(user1.getLogin()).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUserByLogin_NotFound() {
        Optional<User> expected = Optional.empty();

        Optional<User> actual = userRepository.findByLogin(LOGIN_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUsers() {
        User expected1 = entityManager.persist(user1);
        User expected2 = entityManager.persist(user2);
        List<User> expected = Arrays.asList(expected1, expected2);

        List<User> actual = userRepository.getEntities(false);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUsers_Pagination() {
        User expected1 = entityManager.persist(user1);
        entityManager.persist(user2);
        List<User> expected = Collections.singletonList(expected1);

        List<User> actual = userRepository.getEntities(false, PageRequest.of(PAGE_NUMBER_1, PAGE_SIZE_1));

        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void getTheMostUsedTag() {
        Tag tag1 = new Tag();
        tag1.setName(TAG_NAME_1);
        Tag tag2 = new Tag();
        tag2.setName(TAG_NAME_2);

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

        Order order1 = new Order();
        order1.setTotal(ORDER_TOTAL_1);
        order1.setUser(user1);
        order1.setCertificates(certificates1);

        Order order2 = new Order();
        order2.setTotal(ORDER_TOTAL_2);
        order2.setUser(user2);
        order2.setCertificates(certificates2);

        entityManager.persist(tag1);
        entityManager.persist(tag2);
        entityManager.persist(certificate1);
        entityManager.persist(certificate2);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(order1);
        entityManager.persist(order2);

        User expected = user1;

        User actual = userRepository.getUserWithTheLargeSumOrders().stream().findFirst().get();

        assertEquals(expected, actual);
    }
}
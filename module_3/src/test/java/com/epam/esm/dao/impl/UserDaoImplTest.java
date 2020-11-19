package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
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

import java.util.*;

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
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_10 = 10;
    private static final Integer OFFSET_0 = 0;
    private static final Integer OFFSET_2 = 2;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.setName(NAME_1);
        user1.setSurname(SURNAME_1);
        user1.setLock(LOCK);

        user2 = new User();
        user2.setName(NAME_2);
        user2.setSurname(SURNAME_2);
        user2.setLock(LOCK);
    }

    @Test
    public void testGetUserById() {
        User expected = entityManager.persist(user1);

        User actual = userDao.getUserById(expected.getId()).get();

        assertEquals(expected, actual);
        assert actual.getId() > 0;
    }

    @Test
    public void testGetUserById_NotFound() {
        Optional<User> expected = Optional.empty();

        Optional<User> actual = userDao.getUserById(ID_1);

        assertEquals(expected, actual);
    }

    @Test
    public void testUsers() {
        User expected1 = entityManager.persist(user1);
        User expected2 = entityManager.persist(user2);
        List<User> expected = new ArrayList<>(Arrays.asList(expected1, expected2));

        List<User> actual = userDao.getUsers(OFFSET_0, PAGE_SIZE_10);

        Assertions.assertEquals(expected, actual);
        assert !actual.isEmpty();
    }

    @Test
    public void testGetUsers_Pagination() {
        User expected1 = entityManager.persist(user1);
        User expected2 = entityManager.persist(user2);
        List<User> expected = new ArrayList<>(Collections.singletonList(expected1));

        List<User> actual = userDao.getUsers(OFFSET_0, PAGE_SIZE_1);

        Assertions.assertEquals(expected, actual);
        assert !actual.isEmpty();
        assert !actual.contains(expected2);
        assert actual.size() == PAGE_SIZE_1;
    }

    @Test
    public void testGetTags_Pagination_NotFound() {
        entityManager.persist(user1);
        entityManager.persist(user2);

        List<User> actual = userDao.getUsers(OFFSET_2, PAGE_SIZE_10);

        assert actual.isEmpty();
    }
}
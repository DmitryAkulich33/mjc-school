package com.epam.esm.dao.impl;

import com.epam.esm.config.DbConfig;
import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
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

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {DbConfig.class})
@SqlGroup({
        @Sql(scripts = "/drop_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/create_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/init_tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
class UserDaoImplTest {
    private static final Long CORRECT_ID_1 = 1L;
    private static final Long CORRECT_ID_2 = 2L;
    private static final Long WRONG_ID = 10L;
    private static final String NAME_1 = "Ivan";
    private static final String NAME_2 = "Petr";
    private static final String SURNAME_1 = "Ivanov";
    private static final String SURNAME_2 = "Petrov";
    private static final Integer LOCK = 0;
    private static final Integer PAGE_SIZE_1 = 1;
    private static final Integer PAGE_SIZE_10 = 10;
    private static final Integer OFFSET = 0;

    @Autowired
    private UserDao userDao;

    private User user1;
    private User user2;
    private List<User> users;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.setId(CORRECT_ID_1);
        user1.setName(NAME_1);
        user1.setSurname(SURNAME_1);
        user1.setLock(LOCK);

        user2 = new User();
        user2.setId(CORRECT_ID_2);
        user2.setName(NAME_2);
        user2.setSurname(SURNAME_2);
        user2.setLock(LOCK);

        users = new ArrayList<>(Arrays.asList(user1, user2));
    }

    @Test
    public void testGetUserById() {
        Optional<User> expected = Optional.ofNullable(user1);

        Optional<User> actual = userDao.getUserById(CORRECT_ID_1);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void testGetUserById_WrongResult() {
        Optional<User> expected = Optional.ofNullable(user2);

        Optional<User> actual = userDao.getUserById(CORRECT_ID_1);

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void testGetUserById_NotFound() {
        Optional<User> expected = Optional.empty();

        Optional<User> actual = userDao.getUserById(WRONG_ID);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetUsers() {
        List<User> actual = userDao.getUsers(OFFSET, PAGE_SIZE_10);

        Assertions.assertEquals(users, actual);
    }

    @Test
    public void testGetUsers_Pagination() {
        List<User> expected = new ArrayList<>(Collections.singletonList(user1));

        List<User> actual = userDao.getUsers(OFFSET, PAGE_SIZE_1);

        Assertions.assertEquals(expected, actual);
    }
}
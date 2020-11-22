package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserDaoException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Integer OFFSET = 0;

    @Mock
    private UserDao mockUserDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetUserById() {
        User expected = new User();
        expected.setId(USER_ID);

        when(mockUserDao.getUserById(USER_ID)).thenReturn(Optional.of(expected));

        User actual = userService.getUserById(USER_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUserById_UserNotFoundException() {
        when(mockUserDao.getUserById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(USER_ID);
        });
    }

    @Test
    public void testGetUserById_UserDaoException() {
        when(mockUserDao.getUserById(USER_ID)).thenThrow(new UserDaoException());

        assertThrows(UserDaoException.class, () -> {
            userService.getUserById(USER_ID);
        });
    }

    @Test
    public void testGetUsers_WithoutPagination() {
        List<User> expected = new ArrayList<>();

        when(mockUserDao.getUsers()).thenReturn(expected);

        List<User> actual = userService.getUsers(null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUsers_WithoutPagination_UserDaoException() {
        when(mockUserDao.getUsers()).thenThrow(new UserDaoException());

        assertThrows(UserDaoException.class, () -> {
            userService.getUsers(null, null);
        });
    }

    @Test
    public void testGetUsers_WithPagination() {
        List<User> expected = new ArrayList<>();

        when(mockUserDao.getUsers(OFFSET, PAGE_SIZE)).thenReturn(expected);

        List<User> actual = userService.getUsers(PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUsers_WithPagination_UserDaoException() {
        when(mockUserDao.getUsers(OFFSET, PAGE_SIZE)).thenThrow(new UserDaoException());

        assertThrows(UserDaoException.class, () -> {
            userService.getUsers(PAGE_NUMBER, PAGE_SIZE);
        });
    }

    @Test
    public void testGetTags_WithNullPageNumber_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            userService.getUsers(null, PAGE_SIZE);
        });
    }

    @Test
    public void testGetTags_WithNullPageSize_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            userService.getUsers(PAGE_NUMBER, null);
        });
    }
}
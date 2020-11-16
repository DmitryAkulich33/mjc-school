package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserDaoException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.exceptions.UserValidatorException;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.util.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Integer OFFSET = 0;

    @Mock
    private List<User> mockUsers;
    @Mock
    private UserDao mockUserDao;
    @Mock
    private UserValidator mockUserValidator;
    @Mock
    private OffsetCalculator mockOffsetCalculator;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void testGetUsers() {
        when(mockUserDao.getUsers(OFFSET, PAGE_SIZE)).thenReturn(mockUsers);

        List<User> actual = userServiceImpl.getUsers(PAGE_NUMBER, PAGE_SIZE);

        assertEquals(mockUsers, actual);
        verify(mockOffsetCalculator).calculateOffset(PAGE_NUMBER, PAGE_SIZE);
        verify(mockUserDao).getUsers(OFFSET, PAGE_SIZE);
    }

    @Test
    public void testGetUsers_UserDaoException() {
        when(mockUserDao.getUsers(OFFSET, PAGE_SIZE)).thenThrow(new UserDaoException());

        assertThrows(UserDaoException.class, () -> {
            userServiceImpl.getUsers(OFFSET, PAGE_SIZE);
        });
    }

    @Test
    public void testGetUserById() {
        User expected = mock(User.class);

        when(mockUserDao.getUserById(USER_ID)).thenReturn(expected);

        User actual = userServiceImpl.getUserById(USER_ID);

        assertEquals(expected, actual);
        verify(mockUserValidator).validateUserId(USER_ID);
        verify(mockUserDao).getUserById(USER_ID);
    }

    @Test
    public void testGetUserById_UserNotFoundException() {
        when(mockUserDao.getUserById(USER_ID)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> {
            userServiceImpl.getUserById(USER_ID);
        });
    }

    @Test
    public void testGetUserById_UserValidatorException() {
        when(mockUserDao.getUserById(USER_ID)).thenThrow(new UserValidatorException());

        assertThrows(UserValidatorException.class, () -> {
            userServiceImpl.getUserById(USER_ID);
        });
    }

    @Test
    public void testGetTagById_UserDaoException() {
        when(mockUserDao.getUserById(USER_ID)).thenThrow(new UserDaoException());

        assertThrows(UserDaoException.class, () -> {
            userServiceImpl.getUserById(USER_ID);
        });
    }
}
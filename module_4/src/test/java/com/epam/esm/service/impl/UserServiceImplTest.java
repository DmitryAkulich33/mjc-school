package com.epam.esm.service.impl;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final String LOGIN = "user1";
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Long COUNT = 5L;

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetUserById() {
        User expected = new User();
        expected.setId(USER_ID);

        when(mockUserRepository.getEntityById(false, USER_ID)).thenReturn(Optional.of(expected));

        User actual = userService.getUserById(USER_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUserById_UserNotFoundException() {
        when(mockUserRepository.getEntityById(false, USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(USER_ID);
        });
    }

    @Test
    public void testGetUserByLogin() {
        User expected = new User();
        expected.setLogin(LOGIN);

        when(mockUserRepository.findByLogin(LOGIN)).thenReturn(Optional.of(expected));

        User actual = userService.getUserByLogin(LOGIN);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUserByLogin_UserNotFoundException() {
        when(mockUserRepository.findByLogin(LOGIN)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByLogin(LOGIN);
        });
    }

    @Test
    public void testGetUsers_WithoutPagination() {
        List<User> expected = new ArrayList<>();

        when(mockUserRepository.getEntities(false)).thenReturn(expected);

        List<User> actual = userService.getUsers(null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUsers_WithPagination() {
        List<User> expected = new ArrayList<>();

        when(mockUserRepository.getEntities(false, PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(expected);
        when(mockUserRepository.count()).thenReturn(COUNT);

        List<User> actual = userService.getUsers(PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
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
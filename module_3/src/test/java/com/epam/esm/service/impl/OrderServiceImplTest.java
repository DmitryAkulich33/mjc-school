package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.Order;
import com.epam.esm.exceptions.OrderDaoException;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.exceptions.OrderValidatorException;
import com.epam.esm.exceptions.UserValidatorException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.OrderValidator;
import com.epam.esm.util.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
//    private static final Long ID = 1L;
//    private static final Integer PAGE_NUMBER = 1;
//    private static final Integer PAGE_SIZE = 10;
//    private static final Integer OFFSET = 0;
//
//    @Mock
//    private List<Order> mockOrders;
//    @Mock
//    private OrderDao mockOrderDao;
//    @Mock
//    private UserDao mockUserDao;
//    @Mock
//    private OrderValidator mockOrderValidator;
//    @Mock
//    private UserValidator mockUserValidator;
//    @Mock
//
//    private UserService mockUserService;
//
//    @InjectMocks
//    private OrderServiceImpl orderServiceImpl;
//
//    @Test
//    public void testGetOrders() {
//        when(mockOrderDao.getOrders(OFFSET, PAGE_SIZE)).thenReturn(mockOrders);
//
//        List<Order> actual = orderServiceImpl.getOrders(PAGE_NUMBER, PAGE_SIZE);
//
//        assertEquals(mockOrders, actual);
//        verify(mockOffsetCalculator).calculateOffset(PAGE_NUMBER, PAGE_SIZE);
//        verify(mockOrderDao).getOrders(OFFSET, PAGE_SIZE);
//    }
//
//    @Test
//    public void testGetOrders_OrderDaoException() {
//        when(mockOrderDao.getOrders(OFFSET, PAGE_SIZE)).thenThrow(new OrderDaoException());
//
//        assertThrows(OrderDaoException.class, () -> {
//            orderServiceImpl.getOrders(OFFSET, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetOrderById() {
//        Order expected = mock(Order.class);
//
//        when(mockOrderDao.getOrderById(ID)).thenReturn(Optional.ofNullable(expected));
//
//        Order actual = orderServiceImpl.getOrderById(ID);
//
//        assertEquals(expected, actual);
//        verify(mockOrderValidator).validateOrderId(ID);
//        verify(mockOrderDao).getOrderById(ID);
//    }
//
//
//    @Test
//    public void testGetOrderById_OrderValidatorException() {
//        doThrow(OrderValidatorException.class).when(mockOrderValidator).validateOrderId(ID);
//
//        assertThrows(OrderValidatorException.class, () -> {
//            orderServiceImpl.getOrderById(ID);
//        });
//    }
//
//    @Test
//    public void testGetOrderById_OrderDaoException() {
//        when(mockOrderDao.getOrderById(ID)).thenThrow(new OrderDaoException());
//
//        assertThrows(OrderDaoException.class, () -> {
//            orderServiceImpl.getOrderById(ID);
//        });
//    }
//
//    @Test
//    public void testGetDataByUserId_OrderDaoException() {
//        when(mockOrderDao.getOrderDataByUserId(ID, ID)).thenThrow(new OrderDaoException());
//
//        assertThrows(OrderDaoException.class, () -> {
//            orderServiceImpl.getOrderDataByUserId(ID, ID);
//        });
//    }
//
//    @Test
//    public void testGetDataByUserId_UserValidatorException() {
//        doThrow(UserValidatorException.class).when(mockUserValidator).validateUserId(ID);
//        assertThrows(UserValidatorException.class, () -> {
//            orderServiceImpl.getOrderDataByUserId(ID, ID);
//        });
//    }
//
//    @Test
//    public void testGetDataByUserId_OrderValidatorException() {
//        doThrow(OrderValidatorException.class).when(mockOrderValidator).validateOrderId(ID);
//        assertThrows(OrderValidatorException.class, () -> {
//            orderServiceImpl.getOrderDataByUserId(ID, ID);
//        });
//    }
//
//    @Test
//    public void testGetDataByUserId() {
//        Order expected = mock(Order.class);
//
//        when(mockOrderDao.getOrderDataByUserId(ID, ID)).thenReturn(Optional.ofNullable(expected));
//
//        Order actual = orderServiceImpl.getOrderDataByUserId(ID, ID);
//
//        assertEquals(expected, actual);
//
//        verify(mockUserValidator).validateUserId(ID);
//        verify(mockOrderValidator).validateOrderId(ID);
//        verify(mockUserService).getUserById(ID);
//        verify(mockOrderDao).getOrderDataByUserId(ID, ID);
//    }
//
//    @Test
//    public void testGetOrdersByUserId() {
//        orderServiceImpl.getOrdersByUserId(ID, PAGE_NUMBER, PAGE_SIZE);
//
//        verify(mockUserValidator).validateUserId(ID);
//        verify(mockOffsetCalculator).calculateOffset(PAGE_NUMBER, PAGE_SIZE);
//        verify(mockOrderDao).getOrdersByUserId(ID, OFFSET, PAGE_SIZE);
//    }
//
//    @Test
//    public void testGetOrdersUserId_UserValidatorException() {
//        when(mockOrderDao.getOrdersByUserId(ID, OFFSET, PAGE_SIZE)).thenThrow(new UserValidatorException());
//
//        assertThrows(UserValidatorException.class, () -> {
//            orderServiceImpl.getOrdersByUserId(ID, PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetOrdersUserId_OrderNotFoundException() {
//        when(mockOrderDao.getOrdersByUserId(ID, OFFSET, PAGE_SIZE)).thenThrow(new OrderNotFoundException());
//
//        assertThrows(OrderNotFoundException.class, () -> {
//            orderServiceImpl.getOrdersByUserId(ID, PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetOrdersUserId_OrderDaoException() {
//        when(mockOrderDao.getOrdersByUserId(ID, OFFSET, PAGE_SIZE)).thenThrow(new OrderDaoException());
//
//        assertThrows(OrderDaoException.class, () -> {
//            orderServiceImpl.getOrdersByUserId(ID, PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
}
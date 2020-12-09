package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.OrderDaoException;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.exceptions.WrongEnteredDataException;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
//    private static final Long ID = 1L;
//    private static final Integer PAGE_NUMBER = 1;
//    private static final Integer PAGE_SIZE = 10;
//    private static final Integer OFFSET = 0;
//
//    @Mock
//    private OrderDao mockOrderDao;
//    @Mock
//    private UserService mockUserService;
//
//    @Spy
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    @Test
//    public void testGetOrderById() {
//        Order expected = new Order();
//        expected.setId(ID);
//
//        when(mockOrderDao.getOrderById(ID)).thenReturn(Optional.of(expected));
//
//        Order actual = orderService.getOrderById(ID);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrderById_OrderNotFoundException() {
//        when(mockOrderDao.getOrderById(ID)).thenReturn(Optional.empty());
//
//        assertThrows(OrderNotFoundException.class, () -> {
//            orderService.getOrderById(ID);
//        });
//    }
//
//    @Test
//    public void testGetOrderById_OrderDaoException() {
//        when(mockOrderDao.getOrderById(ID)).thenThrow(new OrderDaoException());
//
//        assertThrows(OrderDaoException.class, () -> {
//            orderService.getOrderById(ID);
//        });
//    }
//
//    @Test
//    public void testCreateOrder() {
//        Order expected = new Order();
//        List<Certificate> certificates = new ArrayList<>();
//
//        Double total = 50.0;
//        User user = new User();
//        user.setId(ID);
//        expected.setUser(user);
//        expected.setTotal(total);
//        expected.setCertificates(certificates);
//
//        when(orderService.getTotal(certificates)).thenReturn(total);
//        when(mockUserService.getUserById(ID)).thenReturn(user);
//        when(mockOrderDao.createOrder(expected)).thenReturn(expected);
//
//        Order actual = orderService.makeOrder(ID, certificates);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testCreateOrder_WrongEnteredDataException() {
//        List<Certificate> certificates = new ArrayList<>();
//
//        when(mockUserService.getUserById(ID)).thenThrow(new WrongEnteredDataException());
//
//        assertThrows(WrongEnteredDataException.class, () -> {
//            orderService.makeOrder(ID, certificates);
//        });
//    }
//
//    @Test
//    public void testGetTotal() {
//        Certificate certificate1 = new Certificate();
//        certificate1.setPrice(50.0);
//        Certificate certificate2 = new Certificate();
//        certificate2.setPrice(100.0);
//        List<Certificate> certificates = Arrays.asList(certificate1, certificate2);
//
//        Double expected = 150.0;
//
//        Double actual = orderService.getTotal(certificates);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrders_WithoutPagination() {
//        List<Order> expected = new ArrayList<>();
//
//        when(mockOrderDao.getOrders()).thenReturn(expected);
//
//        List<Order> actual = orderService.getOrders(null, null);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrders_WithoutPagination_OrderDaoException() {
//        when(mockOrderDao.getOrders()).thenThrow(new OrderDaoException());
//
//        assertThrows(OrderDaoException.class, () -> {
//            orderService.getOrders(null, null);
//        });
//    }
//
//    @Test
//    public void testGetOrder_WithPagination() {
//        List<Order> expected = new ArrayList<>();
//
//        when(mockOrderDao.getOrders(OFFSET, PAGE_SIZE)).thenReturn(expected);
//
//        List<Order> actual = orderService.getOrders(PAGE_NUMBER, PAGE_SIZE);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrders_WithPagination_OrderDaoException() {
//        when(mockOrderDao.getOrders(OFFSET, PAGE_SIZE)).thenThrow(new OrderDaoException());
//
//        assertThrows(OrderDaoException.class, () -> {
//            orderService.getOrders(PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetOrders_WithPagination_WrongEnteredDataException() {
//        when(mockOrderDao.getOrders(OFFSET, PAGE_SIZE)).thenThrow(new WrongEnteredDataException());
//
//        assertThrows(WrongEnteredDataException.class, () -> {
//            orderService.getOrders(PAGE_NUMBER, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetOrders_WithNullPageNumber_WrongEnteredDataException() {
//        assertThrows(WrongEnteredDataException.class, () -> {
//            orderService.getOrders(null, PAGE_SIZE);
//        });
//    }
//
//    @Test
//    public void testGetOrders_WithNullPageSize_WrongEnteredDataException() {
//        assertThrows(WrongEnteredDataException.class, () -> {
//            orderService.getOrders(PAGE_NUMBER, null);
//        });
//    }
//
//    @Test
//    public void testGetOrderDataByUserId() {
//        Order expected = new Order();
//
//        User user = new User();
//        user.setId(ID);
//        expected.setUser(user);
//        expected.setId(ID);
//
//        when(mockUserService.getUserById(ID)).thenReturn(user);
//        when(mockOrderDao.getOrderDataByUserId(ID, ID)).thenReturn(Optional.of(expected));
//
//        Order actual = orderService.getOrderDataByUserId(ID, ID);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrderDataByUserId_UserNotFoundException() {
//        when(mockUserService.getUserById(ID)).thenThrow(new UserNotFoundException());
//
//        assertThrows(UserNotFoundException.class, () -> {
//            orderService.getOrderDataByUserId(ID, ID);
//        });
//    }
//
//    @Test
//    public void testGetOrderDataByUserId_OrderNotFoundException() {
//        when(mockOrderDao.getOrderDataByUserId(ID, ID)).thenReturn(Optional.empty());
//
//        assertThrows(OrderNotFoundException.class, () -> {
//            orderService.getOrderDataByUserId(ID, ID);
//        });
//    }
//
//    @Test
//    public void testGetOrdersByUserId_WithPagination() {
//        Order order1 = new Order();
//        Order order2 = new Order();
//
//        List<Order> expected = new ArrayList<>(Arrays.asList(order1, order2));
//
//        when(mockOrderDao.getOrdersByUserId(ID, OFFSET, PAGE_SIZE)).thenReturn(expected);
//
//        List<Order> actual = orderService.getOrdersByUserId(ID, PAGE_NUMBER, PAGE_SIZE);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrdersByUserId_WithoutPagination() {
//        Order order1 = new Order();
//        Order order2 = new Order();
//
//        List<Order> expected = new ArrayList<>(Arrays.asList(order1, order2));
//
//        when(mockOrderDao.getOrdersByUserId(ID)).thenReturn(expected);
//
//        List<Order> actual = orderService.getOrdersByUserId(ID, null, null);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testGetOrdersByUserId_WithNullPageSize_WrongEnteredDataException() {
//        assertThrows(WrongEnteredDataException.class, () -> {
//            orderService.getOrdersByUserId(ID, PAGE_NUMBER, null);
//        });
//    }
//
//    @Test
//    public void testGetOrdersByUserId_WithNullPageNumber_WrongEnteredDataException() {
//        assertThrows(WrongEnteredDataException.class, () -> {
//            orderService.getOrdersByUserId(ID, null, PAGE_SIZE);
//        });
//    }
}
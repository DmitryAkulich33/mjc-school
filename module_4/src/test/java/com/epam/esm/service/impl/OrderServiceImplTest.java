package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderRepository;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
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
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private static final Long ID = 1L;
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer PAGE_SIZE = 10;
    private static final Long COUNT = 5L;

    @Mock
    private OrderRepository mockOrderRepository;
    @Mock
    private UserService mockUserService;

    @Spy
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testGetOrderById() {
        Order expected = new Order();
        expected.setId(ID);

        when(mockOrderRepository.getEntityById(false, ID)).thenReturn(Optional.of(expected));

        Order actual = orderService.getOrderById(ID);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderById_OrderNotFoundException() {
        when(mockOrderRepository.getEntityById(false, ID)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(ID);
        });
    }

    @Test
    public void testCreateOrder() {
        Order expected = new Order();
        List<Certificate> certificates = new ArrayList<>();

        Double total = 50.0;
        User user = new User();
        user.setId(ID);
        expected.setUser(user);
        expected.setTotal(total);
        expected.setCertificates(certificates);

        when(orderService.getTotal(certificates)).thenReturn(total);
        when(mockUserService.getUserById(ID)).thenReturn(user);
        when(mockOrderRepository.save(expected)).thenReturn(expected);

        Order actual = orderService.makeOrder(ID, certificates);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTotal() {
        Certificate certificate1 = new Certificate();
        certificate1.setPrice(50.0);
        Certificate certificate2 = new Certificate();
        certificate2.setPrice(100.0);
        List<Certificate> certificates = Arrays.asList(certificate1, certificate2);

        Double expected = 150.0;

        Double actual = orderService.getTotal(certificates);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrders_WithoutPagination() {
        List<Order> expected = new ArrayList<>();

        when(mockOrderRepository.getEntities(false)).thenReturn(expected);

        List<Order> actual = orderService.getOrders(null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrder_WithPagination() {
        List<Order> expected = new ArrayList<>();

        when(mockOrderRepository.getEntities(false, PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(expected);
        when(mockOrderRepository.count()).thenReturn(COUNT);

        List<Order> actual = orderService.getOrders(PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrders_WithNullPageNumber_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            orderService.getOrders(null, PAGE_SIZE);
        });
    }

    @Test
    public void testGetOrders_WithNullPageSize_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            orderService.getOrders(PAGE_NUMBER, null);
        });
    }

    @Test
    public void testGetOrderDataByUserId() {
        Order expected = new Order();

        User user = new User();
        user.setId(ID);
        expected.setUser(user);
        expected.setId(ID);

        when(mockUserService.getUserById(ID)).thenReturn(user);
        when(mockOrderRepository.findByUserIdAndId(ID, ID)).thenReturn(Optional.of(expected));

        Order actual = orderService.getOrderDataByUserId(ID, ID);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrderDataByUserId_UserNotFoundException() {
        when(mockUserService.getUserById(ID)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.getOrderDataByUserId(ID, ID);
        });
    }

    @Test
    public void testGetOrderDataByUserId_OrderNotFoundException() {
        when(mockOrderRepository.findByUserIdAndId(ID, ID)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderDataByUserId(ID, ID);
        });
    }

    @Test
    public void testGetOrdersByUserId_WithPagination() {
        Order order1 = new Order();
        Order order2 = new Order();

        List<Order> expected = new ArrayList<>(Arrays.asList(order1, order2));

        when(mockOrderRepository.findByUserId(ID, PageRequest.of(PAGE_NUMBER - 1, PAGE_SIZE))).thenReturn(expected);
        when(mockOrderRepository.countAllByUserId(ID)).thenReturn(COUNT);

        List<Order> actual = orderService.getOrdersByUserId(ID, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrdersByUserId_WithoutPagination() {
        Order order1 = new Order();
        Order order2 = new Order();

        List<Order> expected = new ArrayList<>(Arrays.asList(order1, order2));

        when(mockOrderRepository.findByUserId(ID)).thenReturn(expected);

        List<Order> actual = orderService.getOrdersByUserId(ID, null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOrdersByUserId_WithNullPageSize_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            orderService.getOrdersByUserId(ID, PAGE_NUMBER, null);
        });
    }

    @Test
    public void testGetOrdersByUserId_WithNullPageNumber_WrongEnteredDataException() {
        assertThrows(WrongEnteredDataException.class, () -> {
            orderService.getOrdersByUserId(ID, null, PAGE_SIZE);
        });
    }
}
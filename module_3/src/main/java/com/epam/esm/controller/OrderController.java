package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.view.CertificateForOrderView;
import com.epam.esm.view.OrderDataView;
import com.epam.esm.view.OrderView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @JsonView(OrderView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderView> getOrderById(@PathVariable @NonNull Long id) {
        Order order = orderService.getOrderById(id);
        OrderView orderView = OrderView.createForm(order);

        return new ResponseEntity<>(orderView, HttpStatus.OK);
    }

    @JsonView(OrderView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderView>> getOrders() {
        List<Order> orders = orderService.getOrders();
        List<OrderView> ordersView = OrderView.createListForm(orders);

        return new ResponseEntity<>(ordersView, HttpStatus.OK);
    }

    @JsonView(OrderView.Views.V1.class)
    @GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderView>> getOrdersByUserId(@PathVariable @NonNull Long id) {
        List<Order> orders = orderService.getOrdersByUserId(id);
        List<OrderView> ordersView = OrderView.createListForm(orders);

        return new ResponseEntity<>(ordersView, HttpStatus.OK);
    }

    @JsonView(OrderDataView.Views.V1.class)
    @GetMapping(path = "/{idOrder}/users/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDataView> getDataByUserId(@PathVariable @NonNull Long idUser,
                                                         @PathVariable @NonNull Long idOrder) {
        Order order = orderService.getDataByUserId(idUser, idOrder);
        OrderDataView orderDataView = OrderDataView.createForm(order);

        return new ResponseEntity<>(orderDataView, HttpStatus.OK);
    }

    @JsonView(OrderView.Views.V1.class)
    @PostMapping(path = "/toOrder/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderView> makeOrder(@PathVariable @NonNull Long id,
                                               @RequestBody @JsonView(CertificateForOrderView.Views.V1.class) List<CertificateForOrderView> certificates) {
        List<Certificate> certificatesToUpdate = CertificateForOrderView.createListForm(certificates);
        Order order = orderService.makeOrder(id, certificatesToUpdate);
        OrderView orderView = OrderView.createForm(order);

        return new ResponseEntity<>(orderView, HttpStatus.OK);
    }

}

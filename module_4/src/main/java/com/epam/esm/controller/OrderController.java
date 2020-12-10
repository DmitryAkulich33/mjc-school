package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.security.JwtUser;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.view.CertificateForOrderView;
import com.epam.esm.view.OrderDataView;
import com.epam.esm.view.OrderView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @JsonView(OrderView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<OrderView> getOrderById(@PathVariable @NonNull Long id) {
        Order order = orderService.getOrderById(id);
        OrderView orderView = OrderView.createForm(order);

        orderView.add(linkTo(methodOn(OrderController.class).getOrderById(id)).withSelfRel());

        return new ResponseEntity<>(orderView, HttpStatus.OK);
    }

    @JsonView(OrderView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CollectionModel<OrderView>> getOrders(@RequestParam(required = false) @Positive Integer pageNumber,
                                                                @RequestParam(required = false) @Positive Integer pageSize) {
        List<Order> orders = orderService.getOrders(pageNumber, pageSize);
        List<OrderView> ordersView = OrderView.createListForm(orders);

        Link link = linkTo(methodOn(OrderController.class).getOrders(pageNumber, pageSize)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(ordersView, link), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @JsonView(OrderView.Views.V1.class)
    @GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<CollectionModel<OrderView>> getOrdersByUserId(@PathVariable @NonNull Long id,
                                                                        @RequestParam(required = false) @Positive Integer pageNumber,
                                                                        @RequestParam(required = false) @Positive Integer pageSize,
                                                                        @AuthenticationPrincipal JwtUser jwtUser) {
        List<Order> orders;
        List<OrderView> ordersView;
        Link link;
        if(jwtUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            orders = orderService.getOrdersByUserId(id, pageNumber, pageSize);
            ordersView = OrderView.createListForm(orders);
            link = linkTo(methodOn(OrderController.class).getOrdersByUserId(id, pageNumber, pageSize, jwtUser)).withSelfRel();
        } else {
            User user = userService.getUserByLogin(jwtUser.getUsername());
            orders = orderService.getOrdersByUserId(user.getId(), pageNumber, pageSize);
            ordersView = OrderView.createListForm(orders);
            link = linkTo(methodOn(OrderController.class).getOrdersByUserId(user.getId(), pageNumber, pageSize, jwtUser)).withSelfRel();
        }

        return new ResponseEntity<>(CollectionModel.of(ordersView, link), HttpStatus.OK);
    }

    @JsonView(OrderDataView.Views.V1.class)
    @GetMapping(path = "/{idOrder}/users/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<OrderDataView> getDataByUserId(@PathVariable @NotNull @Positive Long idUser,
                                                         @PathVariable @NotNull @Positive Long idOrder) {
        Order order = orderService.getOrderDataByUserId(idUser, idOrder);
        OrderDataView orderDataView = OrderDataView.createForm(order);

        orderDataView.add(linkTo(methodOn(OrderController.class).getDataByUserId(idUser, idOrder)).withSelfRel());

        return new ResponseEntity<>(orderDataView, HttpStatus.OK);
    }

    @JsonView(OrderView.Views.V1.class)
    @PostMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<OrderView> makeOrder(@PathVariable @NotNull @Positive Long id,
                                               @RequestBody @JsonView(CertificateForOrderView.Views.V1.class) List<CertificateForOrderView> certificates) {
        List<Certificate> certificatesToUpdate = CertificateForOrderView.createListForm(certificates);
        Order order = orderService.makeOrder(id, certificatesToUpdate);
        OrderView orderView = OrderView.createForm(order);

        orderView.add(linkTo(methodOn(OrderController.class).makeOrder(id, certificates)).withSelfRel());

        return new ResponseEntity<>(orderView, HttpStatus.OK);
    }
}

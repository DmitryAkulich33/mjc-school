package com.epam.esm.view;

import com.epam.esm.domain.Order;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderView extends RepresentationModel<OrderView> {
    private final String content;
    private static final String ORDERS = "orders";

    @JsonCreator
    public OrderView(@JsonProperty("content") String content) {
        this.content = content;
    }

    @JsonView(Views.V1.class)
    private Long id;

    @JsonView(Views.V1.class)
    private LocalDateTime purchaseDate;

    @JsonView(Views.V1.class)
    private Double total;

    @JsonView(Views.V1.class)
    private UserView user;

    @JsonView(Views.V1.class)
    private List<CertificateView> certificates;

    public class Views {
        public class V1 {
        }
    }

    public static OrderView createForm(Order order) {
        OrderView orderView = new OrderView(ORDERS);
        orderView.setId(order.getId());
        orderView.setPurchaseDate(order.getPurchaseDate());
        orderView.setTotal(order.getTotal());
        orderView.setUser(UserView.createForm(order.getUser()));
        orderView.setCertificates(CertificateView.createListForm(order.getCertificates()));

        return orderView;
    }

    public static List<OrderView> createListForm(List<Order> orders) {
        return orders.stream().map(OrderView::createForm).collect(Collectors.toList());
    }
}

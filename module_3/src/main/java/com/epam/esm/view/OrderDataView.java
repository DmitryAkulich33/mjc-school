package com.epam.esm.view;

import com.epam.esm.domain.Order;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDataView extends RepresentationModel<OrderDataView> {
    private String content;
    private static final String ORDERS = "orders";

    @JsonCreator
    public OrderDataView(@JsonProperty("content") String content) {
        this.content = content;
    }

    @JsonView(Views.V1.class)
    private LocalDateTime purchaseDate;

    @JsonView(Views.V1.class)
    private Double total;

    public class Views {
        public class V1 {
        }
    }

    public static OrderDataView createForm(Order order) {
        OrderDataView orderDataView = new OrderDataView(ORDERS);
        orderDataView.setPurchaseDate(order.getPurchaseDate());
        orderDataView.setTotal(order.getTotal());

        return orderDataView;
    }
}

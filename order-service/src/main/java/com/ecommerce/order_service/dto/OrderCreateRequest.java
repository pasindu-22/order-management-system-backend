package com.ecommerce.order_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OrderCreateRequest {

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private List<OrderItemRequest> items;
}

package com.ecommerce.order_service.dto;

import lombok.Getter;
import lombok.Setter;

public class OrderItemRequest {

    @Getter
    @Setter
    private long productId;

    @Getter
    @Setter
    private int quantity;
}

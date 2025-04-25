package com.ecommerce.order_service.dto;

import lombok.Getter;

public class ProductDto {

    @Getter
    private Long id;

    @Getter
    private String name;

    @Getter
    private Double price;
}

package com.ecommerce.order_service.dto;

import lombok.Getter;
import lombok.Setter;

public class ProductDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Double price;

    @Getter
    @Setter
    private String productSku;
}

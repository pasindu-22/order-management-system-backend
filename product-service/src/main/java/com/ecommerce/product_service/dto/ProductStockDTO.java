package com.ecommerce.product_service.dto;

import com.ecommerce.product_service.model.Product;
import java.math.BigDecimal;
import java.util.UUID;

public class ProductStockDTO {
    private long productId;
    private String name;
    private String sku;
    private String shortDescription;
    private BigDecimal price;
    private String imageUrl;
    private int stockQuantity; // From inventory service

    // Constructor, getters, setters
    public ProductStockDTO(Product product, int stockQuantity) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.sku = product.getSku();
        this.shortDescription = product.getShortDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.stockQuantity = stockQuantity;
    }

    // Getters and setters
}
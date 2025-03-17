package com.ecommerce.inventory_service.dto;

public class StockUpdateDTO {
    private String productSku;
    private int quantity;

    // Getters and Setters
    public String getProductSku() {
        return productSku;
    }

    public int getQuantity() {
        return quantity;
    }
}
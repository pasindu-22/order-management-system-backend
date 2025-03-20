package com.ecommerce.inventory_service.dto;

public class ReservationRequestDTO {
    private String orderId;
    private String productSku;
    private int quantity;

    // Constructors
    public ReservationRequestDTO() {}

    public ReservationRequestDTO(String orderId, String productSku, int quantity) {
        this.orderId = orderId;
        this.productSku = productSku;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
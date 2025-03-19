package com.ecommerce.inventory_service.dto;

import com.ecommerce.inventory_service.model.Stock;

public class StockResponseDTO {
    private Long id;
    private String productSku;
    private int quantity;
    private String warehouseCode;
    private int lowStockThreshold;

    // Constructor to map from Stock entity
    public StockResponseDTO(Stock stock) {
        this.id = stock.getId();
        this.productSku = stock.getProductSku();
        this.quantity = stock.getQuantity();
        this.warehouseCode = stock.getWarehouse() != null ? stock.getWarehouse().getWarehouseCode() : null;
        this.lowStockThreshold = stock.getLowStockThreshold();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getProductSku() {
        return productSku;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }
}
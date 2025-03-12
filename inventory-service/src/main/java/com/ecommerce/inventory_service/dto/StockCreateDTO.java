package com.ecommerce.inventory_service.dto;

public class StockCreateDTO {
    private String productSku;
    private String warehouseCode;  // Changed from Long warehouseId
    private int quantity;
    private int lowStockThreshold;

    // Getters and Setters
    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getWarehouseCode() {  // Updated getter
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {  // Add setter
        this.warehouseCode = warehouseCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {  // Add missing setter
        this.quantity = quantity;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {  // Add missing setter
        this.lowStockThreshold = lowStockThreshold;
    }
}
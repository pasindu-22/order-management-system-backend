package com.ecommerce.inventory_service.dto;

public class WarehouseCreateDTO {
    private String warehouseCode;
    private String location;

    // Getters and Setters
    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
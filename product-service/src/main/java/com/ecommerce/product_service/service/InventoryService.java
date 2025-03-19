package com.ecommerce.product_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class InventoryService {
    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;

    public InventoryService(RestTemplate restTemplate,
                           @Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.restTemplate = restTemplate;
        this.inventoryServiceUrl = inventoryServiceUrl;
    }

    public Integer getStockForProduct(String sku) {
        try {
            return restTemplate.getForObject(
                inventoryServiceUrl + "/api/stock/get/" + sku,
                Integer.class);
        } catch (Exception e) {
            return 0; // Default to 0 if service unavailable
        }
    }
}
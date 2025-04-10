package com.ecommerce.user_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WarehouseVerificationService {
    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;

    public WarehouseVerificationService(RestTemplate restTemplate,
                                      @Value("${services.inventory.url}") String inventoryServiceUrl) {
        this.restTemplate = restTemplate;
        this.inventoryServiceUrl = inventoryServiceUrl;
    }

    public boolean verifyWarehouseExists(Long warehouseId) {
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(
                inventoryServiceUrl + "/api/warehouse/verify/" + warehouseId,
                Boolean.class
            );
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            // Handle connection errors, service unavailability
            return false;
        }
    }
}
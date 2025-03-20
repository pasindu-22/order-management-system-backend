package com.ecommerce.inventory_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ReservationService reservationService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(ReservationService reservationService) {
        this.reservationService = reservationService;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "order.placed", groupId = "inventory-group")
    public void handleOrderPlaced(String message) {
        System.out.println("Received Order placed event: " + message);
        try {
            JsonNode root = objectMapper.readTree(message);
            String orderId = root.path("orderId").asText();
            String productSku = root.path("productSku").asText();
            int quantity = root.path("quantity").asInt();
            System.out.println("Order ID: " + orderId + ", Product SKU: " + productSku + ", Quantity: " + quantity);

            reservationService.reserveInventory(orderId, productSku, quantity);
        } catch (Exception e) {
            System.err.println("Error processing order.placed event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "order.canceled", groupId = "inventory-group")
    public void handleOrderCanceled(String message) {
        System.out.println("Received Order canceled event: " + message);
        try {
            JsonNode root = objectMapper.readTree(message);
            String orderId = root.path("orderId").asText();
            String productSku = root.path("productSku").asText();
            int quantity = root.path("quantity").asInt();

            reservationService.releaseReservedInventory(orderId, productSku, quantity);
        } catch (Exception e) {
            System.err.println("Error processing order.canceled event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "payment.failed", groupId = "inventory-group")
    public void handlePaymentFailed(String message) {
        System.out.println("Received payment failed event: " + message);
        try {
            JsonNode root = objectMapper.readTree(message);
            String orderId = root.path("orderId").asText();
            String productSku = root.path("productSku").asText();
            int quantity = root.path("quantity").asInt();

            reservationService.releaseReservedInventory(orderId, productSku, quantity);
        } catch (Exception e) {
            System.err.println("Error processing payment.failed event: " + e.getMessage());
        }
    }
}
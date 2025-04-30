package com.ecommerce.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order.paid", groupId = "order-group")
    public void handleOrderPaid(String message) {
        System.out.println("Received Order paid event: " + message);
        try {
            JsonNode root = objectMapper.readTree(message);
            Long orderId = root.path("orderId").asLong();
            orderService.orderPaid(orderId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}

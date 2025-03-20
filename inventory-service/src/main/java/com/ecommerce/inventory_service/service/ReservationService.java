package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.model.Stock;
import com.ecommerce.inventory_service.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final KafkaProducerService kafkaProducerService;
    private final StockRepository stockRepository;

    public ReservationService(KafkaProducerService kafkaProducerService, StockRepository stockRepository) {
        this.kafkaProducerService = kafkaProducerService;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public boolean reserveInventory(String orderId, String productSku, int quantity) {
        System.out.println("Reserving stock for Product: " + productSku + ", Quantity: " + quantity);

        // Find stock for the product
        Stock stock = stockRepository.findByProductSku(productSku)
            .orElse(null);

        if (stock == null || stock.getQuantity() < quantity) {
            // Not enough stock available
            return false;
        }

        // Update stock in database
        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);

        // Publish stock updated event
        kafkaProducerService.sendMessage("stock.updated",
            "{ \"productSku\": \"" + productSku + "\", \"quantity\": " + quantity +
            ", \"status\": \"reserved\", \"orderId\": \"" + orderId + "\" }");

        return true;
    }

    @Transactional
    public boolean releaseReservedInventory(String orderId, String productSku, int quantity) {
        System.out.println("Releasing reserved stock for Product: " + productSku + ", Quantity: " + quantity);

        // Find stock for the product
        Stock stock = stockRepository.findByProductSku(productSku)
            .orElse(null);

        if (stock == null) {
            return false;
        }

        // Update stock in database - add the quantity back
        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.save(stock);

        // Publish stock updated event
        kafkaProducerService.sendMessage("stock.updated",
            "{ \"productSku\": \"" + productSku + "\", \"quantity\": " + quantity +
            ", \"status\": \"released\", \"orderId\": \"" + orderId + "\" }");

        return true;
    }
}
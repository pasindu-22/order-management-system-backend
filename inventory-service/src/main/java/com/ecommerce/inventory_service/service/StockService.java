package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.repository.StockRepository;
import com.ecommerce.inventory_service.model.Stock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(UUID stockId) {
        return stockRepository.findById(stockId).orElse(null);
    }

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public void deleteStock(UUID stockId) {
        stockRepository.deleteById(stockId);
    }
}
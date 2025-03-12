package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.StockCreateDTO;
import com.ecommerce.inventory_service.dto.StockUpdateDTO;
import com.ecommerce.inventory_service.model.Warehouse;
import com.ecommerce.inventory_service.repository.StockRepository;
import com.ecommerce.inventory_service.model.Stock;
import com.ecommerce.inventory_service.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final WarehouseRepository warehouseRepository;

    public StockService(StockRepository stockRepository, WarehouseRepository warehouseRepository) {
        this.stockRepository = stockRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(UUID stockId) {
        return stockRepository.findById(stockId).orElse(null);
    }

    public void deleteStock(UUID stockId) {
        stockRepository.deleteById(stockId);
    }


    public String updateStock(StockUpdateDTO updateRequest) {
        Stock currentStock = (Stock) stockRepository.findByProductSku(updateRequest.getProductSku()).orElse(null);
        if (currentStock != null) {
            currentStock.setQuantity(currentStock.getQuantity() + updateRequest.getQuantity());
            stockRepository.save(currentStock);
            return "Stock updated successfully";
        } else {
            return "Stock not found";
        }
    }

    public Stock createStock(StockCreateDTO stockCreateDTO) {
        Warehouse warehouse = warehouseRepository.findByWarehouseCode(stockCreateDTO.getWarehouseCode())
                .orElseThrow(() -> new RuntimeException("Warehouse not found with code: " + stockCreateDTO.getWarehouseCode()));

        Stock stock = Stock.stockDtoTransfer(stockCreateDTO);
        stock.setWarehouse(warehouse);
        return stockRepository.save(stock);
    }
}
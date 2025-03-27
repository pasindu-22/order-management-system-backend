package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.StockCreateDTO;
import com.ecommerce.inventory_service.dto.StockResponseDTO;
import com.ecommerce.inventory_service.dto.StockUpdateDTO;
import com.ecommerce.inventory_service.model.Stock;
import com.ecommerce.inventory_service.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stock")
//    @CrossOrigin(origins = "http://localhost:3000")
public class StockController {
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/new")
    public Stock createStock(@RequestBody StockCreateDTO stockCreateDTO) {
        return stockService.createStock(stockCreateDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateStock(@RequestBody StockUpdateDTO stockUpdateDTO) {
        String result = stockService.updateStock(stockUpdateDTO);
        if ("Stock updated successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

    @GetMapping("/get/all")
    public List<StockResponseDTO> getAllStock() {
        List<Stock> stocks = stockService.getAllStocks();

        return stocks.stream()
                .map(stock -> new StockResponseDTO(stock))
                .collect(Collectors.toList());
    }

    @GetMapping("/get/{sku}")
    public ResponseEntity<Integer> getStockQuantity(@PathVariable String sku) {
        Optional<Stock> stockOpt = Optional.ofNullable(stockService.getStockByProductSku(sku));
        return stockOpt
            .map(stock -> ResponseEntity.ok(stock.getQuantity()))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/available/{sku}")
    public ResponseEntity<Boolean> isStockAvailable(@PathVariable String sku) {
        Optional<Boolean> isAvailable = Optional.ofNullable(stockService.isStockAvailable(sku));
        return isAvailable.map(available -> ResponseEntity.ok(available))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
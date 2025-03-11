package com.ecommerce.inventory_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.inventory_service.service.StockService;


@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    
    @GetMapping
    public String apiCheck() {
        return "Api is OK";
    }


}

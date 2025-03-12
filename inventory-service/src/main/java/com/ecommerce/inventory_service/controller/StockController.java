package com.ecommerce.inventory_service.controller;

    import com.ecommerce.inventory_service.dto.StockCreateDTO;
    import com.ecommerce.inventory_service.dto.StockUpdateDTO;
    import com.ecommerce.inventory_service.model.Stock;
    import com.ecommerce.inventory_service.service.StockService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/stock")
    public class StockController {
        private final StockService stockService;

        @Autowired
        public StockController(StockService stockService) {
            this.stockService = stockService;
        }

        @GetMapping
        public String apiTest() {
            return "API is working!";
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
    }
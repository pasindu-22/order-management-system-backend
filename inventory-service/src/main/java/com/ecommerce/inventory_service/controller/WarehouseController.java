package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.WarehouseCreateDTO;
import com.ecommerce.inventory_service.model.Warehouse;
import com.ecommerce.inventory_service.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String apiTest() {
        return "API is working!";
    }

    @PostMapping("/new")
    public Warehouse createWarehouse(@RequestBody WarehouseCreateDTO warehouseCreateDTO) {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseCode(warehouseCreateDTO.getWarehouseCode());
        warehouse.setLocation(warehouseCreateDTO.getLocation());
        return warehouseService.createWarehouse(warehouse);
    }

    @PutMapping("/update/{id}")
    public Optional<Warehouse> updateWarehouse(@PathVariable Long id, @RequestBody Warehouse warehouseDetails) {
        return warehouseService.updateWarehouse(id, warehouseDetails);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
    }

    @GetMapping("/verify/{id}")
    public ResponseEntity<Boolean> verifyWarehouseExists(@PathVariable Long id) {
        boolean exists = warehouseService.warehouseExists(id);
        return ResponseEntity.ok(exists);
    }

}
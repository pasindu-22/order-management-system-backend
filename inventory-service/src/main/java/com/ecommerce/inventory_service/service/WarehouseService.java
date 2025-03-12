package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.model.Warehouse;
import com.ecommerce.inventory_service.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public Optional<Warehouse> updateWarehouse(Long id, Warehouse warehouseDetails) {
        return warehouseRepository.findById(id).map(warehouse -> {
            warehouse.setWarehouseCode(warehouseDetails.getWarehouseCode());
            warehouse.setLocation(warehouseDetails.getLocation());
            return warehouseRepository.save(warehouse);
        });
    }

    public void deleteWarehouse(Long id) {
        warehouseRepository.deleteById(id);
    }
}
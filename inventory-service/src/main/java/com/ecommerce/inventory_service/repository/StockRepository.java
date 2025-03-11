package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
}
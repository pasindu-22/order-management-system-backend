package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.model.Stock;
import io.micrometer.core.instrument.config.validate.Validated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    Optional<Stock> findByProductSku(String productSku);

    // Get all stocks

}
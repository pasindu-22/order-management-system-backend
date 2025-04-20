package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.product_service.model.Product;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom queries if needed
}

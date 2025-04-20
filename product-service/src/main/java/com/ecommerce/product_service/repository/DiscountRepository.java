package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
